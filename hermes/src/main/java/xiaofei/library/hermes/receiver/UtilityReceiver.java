package xiaofei.library.hermes.receiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import xiaofei.library.hermes.util.ErrorCodes;
import xiaofei.library.hermes.util.HermesException;
import xiaofei.library.hermes.util.TypeUtils;
import xiaofei.library.hermes.wrapper.MethodWrapper;
import xiaofei.library.hermes.wrapper.ObjectWrapper;
import xiaofei.library.hermes.wrapper.ParameterWrapper;

/**
 * Created by Xiaofei on 16/4/8.
 */
public class UtilityReceiver extends Receiver {

    private Method mMethod;

    private Class<?> mClass;

    public UtilityReceiver(ObjectWrapper objectWrapper) throws HermesException {
        super(objectWrapper);
        Class<?> clazz = TYPE_CENTER.getClassType(objectWrapper);
        TypeUtils.validateAccessible(clazz);
        mClass = clazz;
    }

    @Override
    protected void setMethod(MethodWrapper methodWrapper, ParameterWrapper[] parameterWrappers)
            throws HermesException {
        Method method = TYPE_CENTER.getMethod(mClass, methodWrapper);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new HermesException(ErrorCodes.ACCESS_DENIED,
                    "Only static methods can be invoked on the utility class " + mClass.getName()
                            + ". Please modify the method: " + mMethod);
        }
        TypeUtils.validateAccessible(method);
        mMethod = method;
    }

    @Override
    protected Object invokeMethod() throws HermesException {
        Exception exception;
        try {
            return mMethod.invoke(null, getParameters());
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            exception = e;
        }
        exception.printStackTrace();
        throw new HermesException(ErrorCodes.METHOD_INVOCATION_EXCEPTION,
                "Error occurs when invoking method " + mMethod + ".", exception);
    }

}