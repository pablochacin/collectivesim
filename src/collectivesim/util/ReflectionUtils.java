package collectivesim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Methods to simplify reflection.
 * 
 * @author Pablo Chacin
 *
 */
public class ReflectionUtils {


	protected static Map<Class<?>,List<Method>> cache = new HashMap<Class<?>,List<Method>>();


	/***
	 * Invokes a method in a target object. 
	 * 
	 * TODO: due to limitations on how primitive types are handled in java and,
	 * more specifically in the java.lang package, the current implemenation
	 * only works if ALL the arguments of the method are objects. Primitive
	 * types ARE NOT SUPPORTED. See details in {@link #methodMatch(String, Class[], Method)} 
	 * 
	 * @param target
	 * @param methodName
	 * @param args
	 * @throws Exception
	 */
	public static Object invoke(Object target,String methodName,Object[] args) throws Exception{
		Class[] classes = getClasses(args);

		return invoke(target, methodName,classes,args);
	}

	/**
	 * 
	 * @param target
	 * @param methodName
	 * @param classes
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Object invoke(Object target,String methodName,Class[] classes,Object[] args) throws Exception{

		Method method = findMethod(target,methodName, classes);

		return method.invoke(target, args);
	}



	public static Object invokeNative(Object target,String methodName,Object[] args) throws Exception{
		Class[] classes = getClasses(args); 

		Method method = target.getClass().getMethod(methodName, classes);
		//Method method =  target.getClass().getMethod(methodName, classes);
		return method.invoke(target, args);
	}


	public static Class[] getClasses(Object[] args){
		Class[] classes = new Class [0];

		if(args != null){	
			classes = new Class[args.length];
			for(int i=0;i<args.length;i++){
				classes[i] = args[i].getClass();
			}
		}

		return classes;
	}



	private static Method findMethod(Object target,String methodName, Class[] args) throws NoSuchMethodException{

		Method method = null;

		method = findMethodInCache(target, methodName, args);

		if(method == null){
			method = findMethodInClass(target,methodName,args);

			List<Method> methods = cache.get(target.getClass());
			if(methods == null){
				methods = new ArrayList<Method>();
				cache.put(target.getClass(), methods);
			}
			methods.add(method);
			cache.put(target.getClass(), methods);
		}

		return method;
	}

	private static Method findMethodInCache(Object target,String methodName, Class[] args){

		Method method = null;
		List<Method> methods = cache.get(target.getClass());

		if(methods != null){
			for(Method m: methods){
				if(methodMatch(methodName, args, m)){
					method = m;
					break;
				}
			}
		}

		return method;
	}

	/**
	 * Finds the first method, if any, that matches both the given name and signature (type of arguments)
	 * 
	 * @param target
	 * @param method 
	 * @param args array of Class with the classes of the arguments
	 * @return the matching method
	 * @throws NoSuchMethodException
	 */
	private static Method findMethodInClass(Object target,String method, Class[] args) throws NoSuchMethodException{

		Method[] methodes = target.getClass().getMethods();
		for(Method m: methodes){
			if(methodMatch(method,args,m)){
				return m;
			}
		}

		throw new NoSuchMethodException(method);

	}

	/**
	 * Sees if the signature of a Method matches the actual arguments being passed 
	 * to a method invocation.
	 * 
	 * Due to inconsistencies on how Java handles primitive types, methods with
	 * primitive type arguments won't be matched ever. More specifically, java
	 * considers that neither a primitive type is assignable to the corresponding 
	 * Object type (e.g. int to Integer) nor the other way around (Integer to int)
	 * and therefore the test <code>methodArgs[i].isAssignableFrom(args[i]</code> 
	 * is always null.
	 * 
	 * @param name
	 * @param args
	 * @param method
	 * @return
	 */
	private static boolean methodMatch(String name, Class[] args,Method method){
		if(!method.getName().equals(name)){
			return false;
		}

		Class[] methodArgs = method.getParameterTypes();

		if(args.length != methodArgs.length){
			return false;
		}

		for(int i=0;i<args.length;i++){
			if(!methodArgs[i].isAssignableFrom(args[i])){
				return false;
			}
		}

		return true;
	}

	public static Object getAttribute(Object target, String attribute) throws Exception {
		String getter = "get"+attribute;
		return invoke(target,getter,new Object[0]);
	}


	/**
	 * Creates a new instance of the given class calling a constructor that matches the argumens
	 * 
	 * @param type
	 * @param args
	 * @return
	 */
	public static Object createInstance(Class type, Object[] args) throws Exception{

		Class[] classes = getClasses(args);

		Constructor constructor = getCompatibleConstructor(type,classes);

		return constructor.newInstance(args);

	}

	/**
	 * Convenience method. instantiate a class given its name and arguments for a constructor
	 * @param typeName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public static Object createInstance(String typeName, Object ... args) throws Exception{

		Class type = Class.forName(typeName);

		return createInstance(type,args);
	}

	/**
	 * Returns an instance of a given Class from the value in a
	 * String, either using the valueOf static method of the type, if
	 * available, or a constructor with an string argument.
	 * 
	 * @param s
	 * @param type
	 * @return
	 */
	public static  Object parseValue(String value,Class type) {

		Object[] args = {value};

		Object o = null;

		try {
			o = ReflectionUtils.invoke(type, "valueOf", args);

		} catch (Exception e) {
			try {
				o = ReflectionUtils.createInstance(type,args);
			} catch (Exception e1) {
				throw new IllegalArgumentException("It is not possible to instantiate type " + type.getName() +
						"from value "+ value);
			}
		}

		return o;
	}


	/**
	 * Convenience method. Parses a value from a String for type, given its name.
	 *  
	 * @param value
	 * @param typeName
	 * @return
	 */
	public static  Object parseValue(String value,String typeName) {


		try {
			Class type = Class.forName(typeName);
			return parseValue(value,type);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Class not found for type " + typeName);

		}

	}

	/**
	 * Parses a value guessing its type. Currently only Integer, Double, Boolean and String are
	 * supported.
	 * 
	 * @param value
	 * @return
	 */
	public static  Object parseValue(String value) {


		//try as Integer
		try{
			return Integer.valueOf(value);
		}catch(NumberFormatException e) {};

		//try as Double
		try{
			return Double.valueOf(value);
		}catch(NumberFormatException e) {};

		//accept as boolean only "true" or "false"
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")){
			return Boolean.valueOf(value);
		}

		//by default, String
		return String.valueOf(value);

	}


	public static void main(String[] args){

		String[] values = {"2","1.0","true","Hello!"};

		for(String s: values){
			Object value = parseValue(s);			
			System.out.println(value.getClass().getName() + "=" + value.toString());
		}

	}

	/**
	 * Returns a list of getters offered by a class
	 * 
	 * @param target class from which getters will be extracted
	 */
	public static List<Method> getGetters(Class target) {
		List<Method> getters = new ArrayList<Method>();

		for(Method m: target.getMethods()){
			if(Modifier.isPublic(m.getModifiers())){
				if(m.getName().startsWith("get") && (m.getParameterTypes().length == 0)){
					getters.add(m);
				}
			}
		}

		return getters;

	}


	public static Constructor<?> getCompatibleConstructor(Class<?> clazz, Class<?>[] parameterTypes)
	{
		Constructor<?>[] constructors = clazz.getConstructors();
		for (int i = 0; i < constructors.length; i++)
		{
			if (constructors[i].getParameterTypes().length == (parameterTypes != null ? parameterTypes.length : 0))
			{
				// If we have the same number of parameters there is a shot that we have a compatible
				// constructor
				Class<?>[] constructorTypes = constructors[i].getParameterTypes();
				boolean isCompatible = true;
				for (int j = 0; j < (parameterTypes != null ? parameterTypes.length : 0); j++)
				{
					if (!constructorTypes[j].isAssignableFrom(parameterTypes[j]))
					{
						// The type is not assignment compatible, however
						// we might be able to coerce from a basic type to a boxed type
						if (constructorTypes[j].isPrimitive())
						{
							if (!isAssignablePrimitiveToBoxed(constructorTypes[j], parameterTypes[j]))
							{
								isCompatible = false;
								break;
							}
						}
					}
				}
				if (isCompatible)
				{
					return constructors[i];
				}
			}
		}
		return null;
	}

	/**
	 * <p> Checks if a primitive type is assignable with a boxed type.</p>
	 *
	 * @param primitive a primitive class type
	 * @param boxed     a boxed class type
	 *
	 * @return true if primitive and boxed are assignment compatible
	 */
	private static boolean isAssignablePrimitiveToBoxed(Class<?> primitive, Class<?> boxed)
	{
		if (primitive.equals(java.lang.Boolean.TYPE))
		{
			if (boxed.equals(java.lang.Boolean.class))
				return true;
			else
				return false;
		}
		else
		{
			if (primitive.equals(java.lang.Byte.TYPE))
			{
				if (boxed.equals(java.lang.Byte.class))
					return true;
				else
					return false;
			}
			else
			{
				if (primitive.equals(java.lang.Character.TYPE))
				{
					if (boxed.equals(java.lang.Character.class))
						return true;
					else
						return false;
				}
				else
				{
					if (primitive.equals(java.lang.Double.TYPE))
					{
						if (boxed.equals(java.lang.Double.class))
							return true;
						else
							return false;
					}
					else
					{
						if (primitive.equals(java.lang.Float.TYPE))
						{
							if (boxed.equals(java.lang.Float.class))
								return true;
							else
								return false;
						}
						else
						{
							if (primitive.equals(java.lang.Integer.TYPE))
							{
								if (boxed.equals(java.lang.Integer.class))
									return true;
								else
									return false;
							}
							else
							{
								if (primitive.equals(java.lang.Long.TYPE))
								{
									if (boxed.equals(java.lang.Long.class))
										return true;
									else
										return false;
								}
								else
								{
									if (primitive.equals(java.lang.Short.TYPE))
									{
										if (boxed.equals(java.lang.Short.class))
											return true;
										else
											return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
