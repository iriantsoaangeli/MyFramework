package angeli.sprint.utils.reflect;

class TypeChecker {
    static boolean isSimple(Class<?> clazz){
        if(
                clazz.getClass().isPrimitive() ||
                clazz.getClass().equals(String.class) ) {
            return true;
        }
        return false ;
    }
}
