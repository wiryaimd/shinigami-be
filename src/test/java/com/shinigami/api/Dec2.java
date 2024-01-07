/*
 * Developed by Wiryaimd
 * Copyright (c) 2024 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api;

import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class Dec2 {

    private static final String ENCRYPTION_KEY = "467hJ1nA7qt0fMxGPfW3WlD5QuRy1HBTOnukhP9JE467aBTSjD3cSKEJEKMI34mSxRUm98Xu4hXp71YTWJ5lUnP467" + "51d2507871c98d43";
    private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";

//    public static String encrypt(String src) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
//            return Base64.getEncoder().encode(cipher.doFinal(src.getBytes()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static String decrypt(String src) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
            decrypted = new String(cipher.doFinal(src.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    static AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Key makeKey() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    void dec2() {
        String enc = "99B+2zm8RefhZPNCi4DiO1\\/PKO7qTeNMeIoUHxXxEW\\/omBtwl27sPHP9rZ053ri2Wr4K3dSgs0xRyfSCTSiyLYujAPhiaBmyzWrCJoeZLVHEACaljkbmeq7h2V1lrpvwZFYBKGpwpy1WJs16Ae\\/YlQAHf31pLxDZxaxLiQu6FmHGHfu+XZvM+XY4+w+ZWx0XX7JkGHKrrXbI+L7D6dCpKy0avqvd8EfGV79t5OELnKjgLDg1nuoHnXHgP+xqwtGGXkBYen0Al1fkS\\/lh917WiG5rfNV\\/C2VGnsjOAtc5hexzSUoMYXLhRZzHv5OHs61NDY48T+pDXoeXZ6fDa4Uq3i9hlgXetKC+q3XdGvxaCWWdhwtxWQLlW7\\/2ZlCzpF2DoyN+tpdHuMUE9UPNu2+oooDqIc7V97hGqS7PhrsxBYSQUq0MrM0qiXt4wVh0563mPvrkQJLXAEEyOAaRXyvlnJytsl6sFTPsz8i8YyS0JLJYDQiVXuJcPqjvXo6t\\/0IlKAe\\/3Xvt0kU99PrDjxp0c8PYyKiJlB5Gq5H4hh3ga3BXWr1GmUbRd1OTRgj03EcqE5z56CYYpiDuMJUxHR8kYxDNbgggQJX0JKZbRdR4x3FtpBvlt2clRja+W42aoiKEaPsJIk696RtGtczg7BxwLtXm\\/HuMvqKFXnatzGXa0ZATcpncgvMihwCbzxd\\/VP4672eirCuYGkeb0+py5TfnQOcJn4qtveeZbTdmvcvOIQOavL5HebqfrVhKFy7qdO\\/Yi\\/tcy8JVW\\/DppEm0SB\\/SauilC7xaLID1yF4LBeqh4eWU4VQRsOc9ZehUT1n7jvTYzi0htGilkenstTjdtXTZSkuH0m6Tlk1xkwy\\/hXl7Fm9n5inG18WpigmUN1aCQpn54lBl\\/6Zd1p9cUpufhcFxsnJCJ3B5mdvRmVKiwQaYKF8weoSG47knY3ab3HW4+sZY7nw3y4DBmS112vpkww9y\\/E5be1TwwZkZtd5CLfMV94ScwuYUtwGIZg5DnaslZWhFLqb57c417\\/\\/Qt34hdqR9qj0OITMnFMHB+L6XgOOiW\\/+Gjy4xXINOMl4bDA8EVS9aGFsX+ZfTg8IBwF33aLa7UZQb8pdllJClABOk4\\/zDG0QZFP1AAJufI94PEfy54irXvXujmoyWy0h278YnrH0CzjPHe84dzIDsmZq+fBfpIw8EEji56pcnJ3yrwDD7jJmQQFZDg7PpXHgSmj2vU9vL2jCYFyGFuH5d8LnA0zJA1raXe1Q+V7sNrVJeL3UnrA1IlSyoQT1P10rzREjWXafT8Wps1chCXkZRgv8nhnBrTQeRt1lkcabL1kAj9dZEasgu2OBA3+rXlrtoyLQvk\\/tPlMfg5nKbQt3eCbRFkhGzwZcJe1k2fqjHejpTOQePQp78rhQjVdZOTPSmucZWbOazEIBu3xNGL2Xqp3UZvFCh9O3MYRFyefSRuL5dbNVZqUUQi2rXaKpK9pf5BI\\/Pc\\/GbtMcHPu+K0wbcP\\/8dAIusOEF4XtQ7YjKZSL6vvwYVJ1UPdO+51qeMqYk7ZJJpUuqdDTp0+B0VO63KZv6HtftQFTuDSyvjOQwRY8WhvWRm\\/cjuEuLW\\/bH\\/12l\\/cxTLWtNZLqYshTgGs7XOc9vjswVaFW0znmeWyPhoM6JSOuBzhr2iJKKLKBcCjkkXwfFCucqgf2q7w3v7TZOjsoyYTvRWH4VbnKysBzw62Gs\\/JfF64tI1X9jEYHgw0Y68eHX8T+4PdTHVCzTwwswkxyWya51Pl3exHjgs8ImYOPaLmgc5KQ6rQnjJHCZ1hyQgmnOeAj9jgEFenHhNNYzxccMys9eoesjfMk1JEfGKCAKzYiuWOHwclbt0mmg6v8jMV6SQsNDVU9RjHG4+KmckiWbdu2cpZBSNE22xdEB8IzUA48F6ezWuswohTB8txKILhGOPmHWbQ2LiQZ\\/bU6A1foV5fytEOTFsBHfIC\\/JNHq3vXw5Xvprre19xJOzptGzrzVlY+DtUTrsVpDpL92m7DyMfcKQ9RvGiLVcuoJdyiHhGSFWfIBD53s9DdcGnSm44EE7cYDb\\/ElVCQoXgrhrHHr0jUZGWQX8OE054OSysPu\\/pv5WU2Og1jbqpfElbsvkBf6WddbpXmP1+G2r2IHNb5w2WAAi3RFE1bxQBPxyhi9RxXndyCR\\/jjD9KzoBFjnyAiK8QM02wPDJ3y6+h6FAoSGh7byH0DIcg0BH+KRU4+Lz3gBKsC0koY31CqDAU7tQpiqd2S8fBA1YWhc3mpQtZXaoWUZKMqNnocQb\\/xor4V2DClJWB\\/Kf5Htc8842LaApcrJIcr94oyiPZxsJPGateMZltcxDv873RCHTSwn8UqXRWSlQIKX6wKhrSaJhNT1De3UDA+ia6Cl6IaBtbKsXRJC1v5s9GJGCDaqejmyUZcfYXxp58fSL\\/JDJgafd4eiPDAOlcYpvVtAdIeNsFJNHx0XKeEjRXEs39fI\\/Na1xvJM9kQ4isKdsZSB\\/VYf8wS5JhbkAZD04aNuDVaufE4nCWQNb+qv7dnHv4hSXYM6bGHGRYxLbhE4iTRGn5xw6mDzV7jvAB+kgKmmN63o9Jf6qCGMtePCiv361UnrhTxb1J31VVbypdFMk1KXOhYYo0Aq8IU3lJrH72SouWRWBOu+2vTL6PFQnhHJx9nTwOdHaFgMNcqj0U4vlpNrs3Q5Ueuu49U976wVQFosozQCi7ZlPAHCTFPRPL76h\\/Lpvmi2XbpaUN0iee\\/es4sZMISyGWZYsoAd0ejZ5z4bjWvv2MVhPQZy83p7KhS5Fg8aRAGsDkdlgZfEfm4hooxN2IVE+cdoIoGgDMyEbzSnzkuTnvO4hHb0eHZq+8BaNIKNQmheQlewO0rwAIhv\\/\\/fqWgluKRZ7cXfP5E8sxbGgugvmiXHJpDKrFUkM6cvo7tAhvkFIF4+ara\\/4iH\\/9s11iFPKyiXmWgbbkU6eCVMkum1sh9xm6p44ws3KU9H+5q7j3Wgzn0s0kJhgB1TJOEyVAMxDYqNls8jeosz6SCFzjq3eZ9w8pWNB7Ub9SWV9Fvr1Vrdp2zn03tqI1sqdyjv9Hk3+LnT+D1Dh\\/XffelTo3o3uDnj+Q0NjKxJLMdRnGyXlN3ThBqRY9P5OHpENtOnSYyQ6km9efD17Cn10LrH9POzDQWpFirvpPL31YQTElzkcl\\/9NKm5FnVU5NyTN62s9rwh3fxJG2q\\/xzKxQNbFEb\\/7JP34y2Z\\/Q7Xv28FTl2dPKYdMq7dB7JZ30\\/ethghvrgP+Abf3AYTHO\\/Dw1gygJC4oTStOqHaTNkvGxu\\/BnZkUS3evl0W1B6BFR+z3GhgeI6EOqd1hN58J7GTTMue40ncbMCVxwWU0xNkdhyV5RcEM5fnvbaMyO5Rz6\\/LPOJa2WNhIOIVqJXDgbWRP+ODmluiit5sdedec3AJZTOrQi0f94YJkINrh1WNppyxHGTLrA51UnOLeIqCWyQlGJsdP+KIlfj4oLwLyl4GxTyJ6Kv1yOdqR54z6KzsRlqoMDr2FCXUrPAWwq8TBkrm9ZO3VZg7RAIAMY3Ps1NyZA6z87M\\/0Qhlv19R6QESy3DA+8mTte3E+YfRau3rHk5b68fh4Ln+UQrIGX78Sfl5EqHeUpw+myfT4FbB5k7bBF+v4LVRI1Ib6QfusCvUjit4Cwp02J2o27FZNydnLwJnnflyp9zy9hOgQI\\/rJyL8YQxTEwylZbzlIq2lBMq4nECk\\/YCGSMaRSWOboO\\/5D5Ufl9QC+i+3m+lqqrgPq17iCObH5k+9UUi+JwipRJ\\/AODPhAm2rZzBlA0CeX7bHkmG+1i35HBT0SXvlKSWaXQs2oUl2EEZhYB6UQ413Sj6GIajQPXkMxPfiDcnstGq1msvZTlzyAjI+miUlgNBCc4qApzkXph6wkMyYPMT0+dzi03GXYKJcowKG6\\/SPqhAwMtMJ1z6HLrJLZgeEtAZl2EzH87neISmSsusWVAQMxE2RSx3cn7KUWRR80rJ\\/YLhiAVkuPkWO2F\\/eY2Nn3Jb0jVcBs8zfuvjK7fT\\/wfEzAMUbhcGq8KzBweTR4HjIBWnYDHITWm1WGBp4og6pNDLI948jWCT6ln9Z4k6bmmo2psG\\/59aX04BGXvItuqHDA3je8krsW3xws1e+bLI+JT7nWgqndQs7yGNDAXp1nuKtghZ4C2f9gF1aq5IKov2uMgVKChco4C5uG4yg\\/vuQWwPdgRiUa4hVY+DSH";
        String res = decrypt(enc);
        System.out.println(res);
    }
}
