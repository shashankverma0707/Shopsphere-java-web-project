package com.ecommerce.util;
import org.mindrot.jbcrypt.BCrypt;
public final class PasswordUtil { private PasswordUtil(){} public static String hash(String p){return BCrypt.hashpw(p,BCrypt.gensalt());} public static boolean matches(String p,String hash){return BCrypt.checkpw(p,hash);} }
