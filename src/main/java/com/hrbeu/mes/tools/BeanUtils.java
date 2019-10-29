package com.hrbeu.mes.tools;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
    private static BeanUtilsBean beanUtilsBean;

    static {
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new LongConverter((Object) null), Long.class);
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        } else {
            if (o instanceof String) {
                if (((String) o).trim().length() == 0) {
                    return true;
                }
            } else if (o instanceof Collection) {
                if (((Collection) o).isEmpty()) {
                    return true;
                }
            } else if (o.getClass().isArray()) {
                if (((Object[]) o).length == 0) {
                    return true;
                }
            } else if (o instanceof Map) {
                if (((Map) o).isEmpty()) {
                    return true;
                }
            } else if (o instanceof Long) {
                if ((Long) o == null) {
                    return true;
                }
            } else {
                if (!(o instanceof Short)) {
                    return false;
                }

                if ((Short) o == null) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isNotEmpty(Long o) {
        return !isEmpty(o);
    }

    public static boolean isNumber(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Number) {
            return true;
        } else if (o instanceof String) {
            try {
                Double.parseDouble((String) o);
                return true;
            } catch (NumberFormatException arg1) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Object populateEntity(Map map, Object entity)
            throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(entity, map);
        return entity;
    }

    public static boolean validClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException arg1) {
            return false;
        }
    }

    public static boolean isInherit(Class cls, Class parentClass) {
        return parentClass.isAssignableFrom(cls);
    }

    public static Object cloneBean(Object bean) {
        try {
            return beanUtilsBean.cloneBean(bean);
        } catch (Exception arg1) {
            handleReflectionException(arg1);
            return null;
        }
    }

    public static Object getBean(Class cls) {
        WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
        return ctx.getBean(cls);
    }

    public static List<String> scanPackages(String basePackages) throws IllegalArgumentException {
        PathMatchingResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(rl);
        ArrayList result = new ArrayList();
        String[] arrayPackages = basePackages.split(",");

        try {
            for (int e = 0; e < arrayPackages.length; ++e) {
                String packageToScan = arrayPackages[e];
                String packagePart = packageToScan.replace('.', '/');
                String classPattern = "classpath*:/" + packagePart + "/**/*.class";
                Resource[] resources = rl.getResources(classPattern);

                for (int i = 0; i < resources.length; ++i) {
                    Resource resource = resources[i];
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    result.add(className);
                }
            }
        } catch (Exception arg13) {
            new IllegalArgumentException("scan pakcage class error,pakcages:" + basePackages);
        }

        return result;
    }

    public static void copyNotNullProperties(Object dest, Object orig) {
        if (dest == null) {
            logger.error("No destination bean specified");
        } else if (orig == null) {
            logger.error("No origin bean specified");
        } else {
            try {
                int i;
                String name;
                Object e;
                if (orig instanceof DynaBean) {
                    DynaProperty[] ex = ((DynaBean) orig).getDynaClass().getDynaProperties();

                    for (i = 0; i < ex.length; ++i) {
                        name = ex[i].getName();
                        if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
                                && beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
                            e = ((DynaBean) orig).get(name);
                            beanUtilsBean.copyProperty(dest, name, e);
                        }
                    }
                } else if (orig instanceof Map) {
                    Iterator arg7 = ((Map) orig).entrySet().iterator();

                    while (arg7.hasNext()) {
                        Entry arg9 = (Entry) arg7.next();
                        name = (String) arg9.getKey();
                        if (beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
                            beanUtilsBean.copyProperty(dest, name, arg9.getValue());
                        }
                    }
                } else {
                    PropertyDescriptor[] arg8 = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(orig);

                    for (i = 0; i < arg8.length; ++i) {
                        name = arg8[i].getName();
                        if (!"class".equals(name) && beanUtilsBean.getPropertyUtils().isReadable(orig, name)
                                && beanUtilsBean.getPropertyUtils().isWriteable(dest, name)) {
                            try {
                                e = beanUtilsBean.getPropertyUtils().getSimpleProperty(orig, name);
                                if (e != null) {
                                    beanUtilsBean.copyProperty(dest, name, e);
                                }
                            } catch (NoSuchMethodException arg5) {
                                arg5.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception arg6) {
                handleReflectionException(arg6);
            }

        }
    }

    public static <T> T copyProperties(Class<T> destClass, Object orig) {
        Object target = null;

        try {
            target = destClass.newInstance();
            copyProperties(target, orig);
            return (T) target;
        } catch (Exception arg3) {
            handleReflectionException(arg3);
            return null;
        }
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            beanUtilsBean.copyProperties(dest, orig);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
        }

    }

    public static void copyProperty(Object bean, String name, Object value) {
        try {
            beanUtilsBean.copyProperty(bean, name, value);
        } catch (Exception arg3) {
            handleReflectionException(arg3);
        }

    }

    public static Map describe(Object bean) {
        try {
            return beanUtilsBean.describe(bean);
        } catch (Exception arg1) {
            handleReflectionException(arg1);
            return null;
        }
    }

    public static String[] getArrayProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getArrayProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static ConvertUtilsBean getConvertUtils() {
        return beanUtilsBean.getConvertUtils();
    }

    public static String getIndexedProperty(Object bean, String name, int index) {
        try {
            return beanUtilsBean.getIndexedProperty(bean, name, index);
        } catch (Exception arg3) {
            handleReflectionException(arg3);
            return null;
        }
    }

    public static String getIndexedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getIndexedProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static String getMappedProperty(Object bean, String name, String key) {
        try {
            return beanUtilsBean.getMappedProperty(bean, name, key);
        } catch (Exception arg3) {
            handleReflectionException(arg3);
            return null;
        }
    }

    public static String getMappedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getMappedProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static String getNestedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getNestedProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static String getProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static PropertyUtilsBean getPropertyUtils() {
        try {
            return beanUtilsBean.getPropertyUtils();
        } catch (Exception arg0) {
            handleReflectionException(arg0);
            return null;
        }
    }

    public static String getSimpleProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getSimpleProperty(bean, name);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
            return null;
        }
    }

    public static void populate(Object bean, Map properties) {
        try {
            beanUtilsBean.populate(bean, properties);
        } catch (Exception arg2) {
            handleReflectionException(arg2);
        }

    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            beanUtilsBean.setProperty(bean, name, value);
        } catch (Exception arg3) {
            handleReflectionException(arg3);
        }

    }

    private static void handleReflectionException(Exception e) {
        ReflectionUtils.handleReflectionException(e);
    }
}