/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

//@SpringBootTest
public class Dec1 {

//    @Autowired
//    private ResourceLoader resourceLoader;

//    @Test
//    void js1() throws IOException, ScriptException {
//        String key = "467hJ1nA7qt0fMxGPfW3WlD5QuRy1HBTOnukhP9JE467aBTSjD3cSKEJEKMI34mSxRUm98Xu4hXp71YTWJ5lUnP467";
////        String key = "27066hJ1nA7qt0fMxGPfW3WlD5QuRy1HBTOnukhP9JE27066aBTSjD3cSKEJEKMI34mSxRUm98Xu4hXp71YTWJ5lUnP27066";
//        String iv = "3232163e4bca671f2d7cd5a901d5a689";
////        String iv = "1376bb431cab87394bfe0cc762f26496";
//
////        String salt = "ac62076f94323dd7";
//        String salt = "51d2507871c98d43";
////        key = key + salt;
//
//        String ct = "99B+2zm8RefhZPNCi4DiO1\\/PKO7qTeNMeIoUHxXxEW\\/omBtwl27sPHP9rZ053ri2Wr4K3dSgs0xRyfSCTSiyLYujAPhiaBmyzWrCJoeZLVHEACaljkbmeq7h2V1lrpvwZFYBKGpwpy1WJs16Ae\\/YlQAHf31pLxDZxaxLiQu6FmHGHfu+XZvM+XY4+w+ZWx0XX7JkGHKrrXbI+L7D6dCpKy0avqvd8EfGV79t5OELnKjgLDg1nuoHnXHgP+xqwtGGXkBYen0Al1fkS\\/lh917WiG5rfNV\\/C2VGnsjOAtc5hexzSUoMYXLhRZzHv5OHs61NDY48T+pDXoeXZ6fDa4Uq3i9hlgXetKC+q3XdGvxaCWWdhwtxWQLlW7\\/2ZlCzpF2DoyN+tpdHuMUE9UPNu2+oooDqIc7V97hGqS7PhrsxBYSQUq0MrM0qiXt4wVh0563mPvrkQJLXAEEyOAaRXyvlnJytsl6sFTPsz8i8YyS0JLJYDQiVXuJcPqjvXo6t\\/0IlKAe\\/3Xvt0kU99PrDjxp0c8PYyKiJlB5Gq5H4hh3ga3BXWr1GmUbRd1OTRgj03EcqE5z56CYYpiDuMJUxHR8kYxDNbgggQJX0JKZbRdR4x3FtpBvlt2clRja+W42aoiKEaPsJIk696RtGtczg7BxwLtXm\\/HuMvqKFXnatzGXa0ZATcpncgvMihwCbzxd\\/VP4672eirCuYGkeb0+py5TfnQOcJn4qtveeZbTdmvcvOIQOavL5HebqfrVhKFy7qdO\\/Yi\\/tcy8JVW\\/DppEm0SB\\/SauilC7xaLID1yF4LBeqh4eWU4VQRsOc9ZehUT1n7jvTYzi0htGilkenstTjdtXTZSkuH0m6Tlk1xkwy\\/hXl7Fm9n5inG18WpigmUN1aCQpn54lBl\\/6Zd1p9cUpufhcFxsnJCJ3B5mdvRmVKiwQaYKF8weoSG47knY3ab3HW4+sZY7nw3y4DBmS112vpkww9y\\/E5be1TwwZkZtd5CLfMV94ScwuYUtwGIZg5DnaslZWhFLqb57c417\\/\\/Qt34hdqR9qj0OITMnFMHB+L6XgOOiW\\/+Gjy4xXINOMl4bDA8EVS9aGFsX+ZfTg8IBwF33aLa7UZQb8pdllJClABOk4\\/zDG0QZFP1AAJufI94PEfy54irXvXujmoyWy0h278YnrH0CzjPHe84dzIDsmZq+fBfpIw8EEji56pcnJ3yrwDD7jJmQQFZDg7PpXHgSmj2vU9vL2jCYFyGFuH5d8LnA0zJA1raXe1Q+V7sNrVJeL3UnrA1IlSyoQT1P10rzREjWXafT8Wps1chCXkZRgv8nhnBrTQeRt1lkcabL1kAj9dZEasgu2OBA3+rXlrtoyLQvk\\/tPlMfg5nKbQt3eCbRFkhGzwZcJe1k2fqjHejpTOQePQp78rhQjVdZOTPSmucZWbOazEIBu3xNGL2Xqp3UZvFCh9O3MYRFyefSRuL5dbNVZqUUQi2rXaKpK9pf5BI\\/Pc\\/GbtMcHPu+K0wbcP\\/8dAIusOEF4XtQ7YjKZSL6vvwYVJ1UPdO+51qeMqYk7ZJJpUuqdDTp0+B0VO63KZv6HtftQFTuDSyvjOQwRY8WhvWRm\\/cjuEuLW\\/bH\\/12l\\/cxTLWtNZLqYshTgGs7XOc9vjswVaFW0znmeWyPhoM6JSOuBzhr2iJKKLKBcCjkkXwfFCucqgf2q7w3v7TZOjsoyYTvRWH4VbnKysBzw62Gs\\/JfF64tI1X9jEYHgw0Y68eHX8T+4PdTHVCzTwwswkxyWya51Pl3exHjgs8ImYOPaLmgc5KQ6rQnjJHCZ1hyQgmnOeAj9jgEFenHhNNYzxccMys9eoesjfMk1JEfGKCAKzYiuWOHwclbt0mmg6v8jMV6SQsNDVU9RjHG4+KmckiWbdu2cpZBSNE22xdEB8IzUA48F6ezWuswohTB8txKILhGOPmHWbQ2LiQZ\\/bU6A1foV5fytEOTFsBHfIC\\/JNHq3vXw5Xvprre19xJOzptGzrzVlY+DtUTrsVpDpL92m7DyMfcKQ9RvGiLVcuoJdyiHhGSFWfIBD53s9DdcGnSm44EE7cYDb\\/ElVCQoXgrhrHHr0jUZGWQX8OE054OSysPu\\/pv5WU2Og1jbqpfElbsvkBf6WddbpXmP1+G2r2IHNb5w2WAAi3RFE1bxQBPxyhi9RxXndyCR\\/jjD9KzoBFjnyAiK8QM02wPDJ3y6+h6FAoSGh7byH0DIcg0BH+KRU4+Lz3gBKsC0koY31CqDAU7tQpiqd2S8fBA1YWhc3mpQtZXaoWUZKMqNnocQb\\/xor4V2DClJWB\\/Kf5Htc8842LaApcrJIcr94oyiPZxsJPGateMZltcxDv873RCHTSwn8UqXRWSlQIKX6wKhrSaJhNT1De3UDA+ia6Cl6IaBtbKsXRJC1v5s9GJGCDaqejmyUZcfYXxp58fSL\\/JDJgafd4eiPDAOlcYpvVtAdIeNsFJNHx0XKeEjRXEs39fI\\/Na1xvJM9kQ4isKdsZSB\\/VYf8wS5JhbkAZD04aNuDVaufE4nCWQNb+qv7dnHv4hSXYM6bGHGRYxLbhE4iTRGn5xw6mDzV7jvAB+kgKmmN63o9Jf6qCGMtePCiv361UnrhTxb1J31VVbypdFMk1KXOhYYo0Aq8IU3lJrH72SouWRWBOu+2vTL6PFQnhHJx9nTwOdHaFgMNcqj0U4vlpNrs3Q5Ueuu49U976wVQFosozQCi7ZlPAHCTFPRPL76h\\/Lpvmi2XbpaUN0iee\\/es4sZMISyGWZYsoAd0ejZ5z4bjWvv2MVhPQZy83p7KhS5Fg8aRAGsDkdlgZfEfm4hooxN2IVE+cdoIoGgDMyEbzSnzkuTnvO4hHb0eHZq+8BaNIKNQmheQlewO0rwAIhv\\/\\/fqWgluKRZ7cXfP5E8sxbGgugvmiXHJpDKrFUkM6cvo7tAhvkFIF4+ara\\/4iH\\/9s11iFPKyiXmWgbbkU6eCVMkum1sh9xm6p44ws3KU9H+5q7j3Wgzn0s0kJhgB1TJOEyVAMxDYqNls8jeosz6SCFzjq3eZ9w8pWNB7Ub9SWV9Fvr1Vrdp2zn03tqI1sqdyjv9Hk3+LnT+D1Dh\\/XffelTo3o3uDnj+Q0NjKxJLMdRnGyXlN3ThBqRY9P5OHpENtOnSYyQ6km9efD17Cn10LrH9POzDQWpFirvpPL31YQTElzkcl\\/9NKm5FnVU5NyTN62s9rwh3fxJG2q\\/xzKxQNbFEb\\/7JP34y2Z\\/Q7Xv28FTl2dPKYdMq7dB7JZ30\\/ethghvrgP+Abf3AYTHO\\/Dw1gygJC4oTStOqHaTNkvGxu\\/BnZkUS3evl0W1B6BFR+z3GhgeI6EOqd1hN58J7GTTMue40ncbMCVxwWU0xNkdhyV5RcEM5fnvbaMyO5Rz6\\/LPOJa2WNhIOIVqJXDgbWRP+ODmluiit5sdedec3AJZTOrQi0f94YJkINrh1WNppyxHGTLrA51UnOLeIqCWyQlGJsdP+KIlfj4oLwLyl4GxTyJ6Kv1yOdqR54z6KzsRlqoMDr2FCXUrPAWwq8TBkrm9ZO3VZg7RAIAMY3Ps1NyZA6z87M\\/0Qhlv19R6QESy3DA+8mTte3E+YfRau3rHk5b68fh4Ln+UQrIGX78Sfl5EqHeUpw+myfT4FbB5k7bBF+v4LVRI1Ib6QfusCvUjit4Cwp02J2o27FZNydnLwJnnflyp9zy9hOgQI\\/rJyL8YQxTEwylZbzlIq2lBMq4nECk\\/YCGSMaRSWOboO\\/5D5Ufl9QC+i+3m+lqqrgPq17iCObH5k+9UUi+JwipRJ\\/AODPhAm2rZzBlA0CeX7bHkmG+1i35HBT0SXvlKSWaXQs2oUl2EEZhYB6UQ413Sj6GIajQPXkMxPfiDcnstGq1msvZTlzyAjI+miUlgNBCc4qApzkXph6wkMyYPMT0+dzi03GXYKJcowKG6\\/SPqhAwMtMJ1z6HLrJLZgeEtAZl2EzH87neISmSsusWVAQMxE2RSx3cn7KUWRR80rJ\\/YLhiAVkuPkWO2F\\/eY2Nn3Jb0jVcBs8zfuvjK7fT\\/wfEzAMUbhcGq8KzBweTR4HjIBWnYDHITWm1WGBp4og6pNDLI948jWCT6ln9Z4k6bmmo2psG\\/59aX04BGXvItuqHDA3je8krsW3xws1e+bLI+JT7nWgqndQs7yGNDAXp1nuKtghZ4C2f9gF1aq5IKov2uMgVKChco4C5uG4yg\\/vuQWwPdgRiUa4hVY+DSH";
//
//        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
//        ScriptEngine engine = scriptEngineManager.getEngineByName("graal.js");
////        Resource resource = resourceLoader.getResource("classpath:/js/crypto-js/crypto-js.js");
//        Resource resAes = resourceLoader.getResource("classpath:/js/enc1.js");
//
////        engine.eval(resource.getContentAsString(StandardCharsets.UTF_8));
//        engine.eval(resAes.getContentAsString(StandardCharsets.UTF_8));
//        String script = String.format("""
//                let b = {
//                    "ct": "%s",
//                    "iv": "%s",
//                    "s": "%s"
//                };
//                let body = JSON.stringify(b);
//
//                let key = "%s"
//
//                var result = CryptoJSAesJson.decrypt(body, key);
//                result;
//                """, ct, iv, salt, key);
//        String res = (String) engine.eval(script);
//        System.out.println("res bor: " + res);
//    }

    @Test
    void a() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        String key = "467hJ1nA7qt0fMxGPfW3WlD5QuRy1HBTOnukhP9JE467aBTSjD3cSKEJEKMI34mSxRUm98Xu4hXp71YTWJ5lUnP467";
//        String key = "27066hJ1nA7qt0fMxGPfW3WlD5QuRy1HBTOnukhP9JE27066aBTSjD3cSKEJEKMI34mSxRUm98Xu4hXp71YTWJ5lUnP27066";
        String iv = "3232163e4bca671f2d7cd5a901d5a689";
//        String iv = "1376bb431cab87394bfe0cc762f26496";

//        String salt = "ac62076f94323dd7";
        String salt = "51d2507871c98d43";
//        key = key + salt;

        byte[] decodedKey = key.getBytes();
        byte[] decodedIv = iv.getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        /* PBEKeySpec class implements KeySpec interface. */
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);

        SecretKey originalKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(decodedIv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, originalKey, ivParameterSpec);

//        String encryptedText = "y+kX7sOfQCeO++KCxUVCKAVrOldy3vOTwOdkM8Vya0BKKIanmcyBKYwqhZ0\\/7l9IBercJtSYA48qzhwNzT1pOz+fePVzc2MIX7AgsHyfiNCLwds1aaPsXCK3I+FWoS2SuZZq1fyTfWnqimIIe65jHUPs+ZmxTDeLcEo0l5WAjnttcb45+\\/ONfEzcWzf9Hv1FJgIaWUMkJPS\\/QmewdVhhX3CBPpfOKz12il49YeBMFMQiTqGGD\\/o1R\\/vXJo8SLzy7LKbupCLfXViev57L6LjhIycbke0J5CJxCRlLm6\\/KdXcLY0Sx+KRcZC6vk+yuunMZevfdpGRylRRFfjhW5MG6iiQp5ux\\/Rzw3WrGFrJP0rMLM44o\\/Vc\\/QgoJHfQ2NwngIGM7eWJXeCUR7XbB9mgDFTF\\/czlvFC8na8gIwYSxXh6yVjIUtaUpVhz9mQT735\\/SGtjwMJivHIz4o7PrcfUVBhrjoGprIrQXMokYYCMcAEFQU\\/MTBJ5HHHNsBDSw+Gs7lDIs6ozcy8wOg48zPI+fuBo0ke+c5IIUT8cXj8\\/Tddx95MHJtpl5zFG7J9Xx0jDSAdCum2xeK2w+49elejBojmR24oIj9vBv+aQ3lT5SzthskExk5cpTtpMV3txK9Dlnfe5XqzQcDY+f4psKIQDe+NNz5Vp7Ny\\/BhPrme41OR+JFJMkWCpooRc7jOQ6QE8HoRKDPlTZzk4HTltPS9hXnZRRfh6K6ArsxtsRDOT+3Uh\\/fp3PF20pDcJVSPxqkJXTxC1yUSY8naL6rLH4JGa6KRO4etGdvCJc7UmpARqRm0xl9Xs+WWhKZ7UTPnHhoPKrMaz5HuQQz\\/5pMeLI8l9q5HgCvcKUYh2paQVNbvfuYAFYRxMPg7oyV8zHZq9w6Z\\/5Pbz64iSMnIvKC75P\\/V96ZslbQfwqrUhacW7DEO1i54TFjZle+Zb5R\\/q4H9eNXthfuyyba9rCnBN22hjzUx0eF34wUD3Xvd\\/6IdZctMIc6H2I1ld0vwSJC0GICbHLPGmXc39oKrfPcvVpZzljtPrb77w1wnJQeaK8+jy7D3v+lEYpXpMJ\\/qNiD4fl+XsU8004rGVtxbnduldFnqOpdDZQgxrzMT0kqJBmkyGLEcGcoZFbg1cLKZUCpRpPdZ1xLt4dHvviyl9K4LlqWclHy1C8B7l8olVi9CMPX\\/pFTOddXEkXo84qMYmA1+Y6oqSJbCwZ51qJATbPD0LeE99jIVsnscroxmb0AVkxF2et843v8SSVsdUyJxkbMTBDtEhrOEEMYJ9AepFfsXFDEy\\/9P\\/nPq7NqGRBftBIx9D\\/VCJWqD\\/xLQZ6Nal4hnbEdkUpBiyDvl5B3kz+utVbDKovI++izWtjxCKAc1W60htOs+PLPs\\/wYfhhIYbYlWy6LQK3iGW650vE+bHz\\/gNVHg+qW8EKkfXeNCbDnNuNeICB9Cy4aK3eKmS4FSLnLMEjIBAyhYDvvXqXiD5vvzllSb1fX\\/yDiEgWlNzRfK4T\\/fmxhxBTPmu8E\\/w25xwxbspozfJ8vs1WDtJa3sioKXLQ6h+bjnQlvalyquHRbpZesaYi9aByfVZb0lMDRiGLla84qnPKMLzclutq+fF2jiG5YLkZnFF7RZEIYGD0UihD\\/CgjFhbDEo3pxn+Oh5TE4+Aq5fhLOFS+Z7\\/wBXC3MzCdBoZUpu9uVRK0tG3NPry\\/cWbdOi6RSy+oZs2fSUdUA+hkcunGoFUym9+Gr0HGZvs1ALevf+eaHOdl9ZVCU8xC3b9lYLN0DzBbzGjXhogTEMe1oc7JKUb5C2OIHjmHAQEc3UULWci6X7ovla8zaaXqQ86B8Fy3wvBgElLpzFHrGgre9Bt9mlYi5OqC6RKWfd+djGFgSSHPcERcru6Imnwc7uKypv8MAP\\/1fWyP0LUHoAFASVoSlnOiZK1Y4uqdqm9J5eMRicX\\/agIQljMWnq+LpphACCw4m\\/hmkT3WEpeQpehPtpQO3zr1KkfqhOeXafAwmbW8R5am7G2u8uM43GX+3BCTUqP+lGlD+739KazwZ80bt9WBUrTt0Vl0JaNiGlKgQbK154xggAD6\\/uGxqXL5RmDtYF7raZMI+yssqAbyqn7bpf3YkeBZFRqCyh2Tz9pMMkAQaqcJsRXnc7xq1BpmT5d6\\/elhxXcqmjVFFaz378ka8aXgxNiDuE+0VeAtVt5FfU1u2VJoismvd9OIme18R\\/zcVu8ahC2BRq4k1tlbktfSamA7C8Rf8wsZXhcw6EEoiMcCo+PHfSRcZOp1OEK8N0CnG97uUkAeq+chYll8rFausHIwMNt882R0uarWWUI2eniAyT0VuitR0jpsOjYPxJj4UoaOlg6\\/8OQ3CPrRtZS21rmEaMyh9hEEFjvLI4N1WJeeypuSG8pI\\/a4x9JYdrJl3YRFOdFNoV0eEB6k2hVhdz90PaqfMZfvoCTdGDe1EY\\/Lg33zijxrkA42Cn7C9Wa1XomwRQmAFyF3AqkizsnRjacnlSRyWg62mJ1s\\/m7rEXGHjo3XmH5l+eSzhfOzmUqOv3SpppYJAWveP\\/18UzDZ\\/cIp5qxMxCmI+zAsIgUO9M0lPnhhchOvCHnORlw4Bp0WN8sL0IjmEoQde7NUmvFTv3CIwt\\/RvUEwf5Ncw7+vKc+dO96nFrmxxexFTFYhXxvjHD1BPdgngpbzN83MKPhYRPUr4x03nwSg9cnbh\\/H9aiYqcoNdAQtiq3X20y866Xa07EI+5iTLDkkwPvFVkFpes5a5u7hr4Y4PUNjWidPpoVIodxrgEiAtDopjSrSZLIQ4l65wS3JA8so9ziHN7f3aULqvqTn2DRXe79RJC33CWbQJK0rwE2S04IxqpqNyjHBHY4m6A9suPVj0Rh735jauyxcJLIjeWxgkPH\\/TW9GptChSHM7wgmdhKGYnIAtJ+rfcmcvHL17DmsLA5PM1tC7umlvfelNb2LqAwtL2Lz5fI3kwQEogpdIqv97TShMfIXwSbO57ZyHrwk5dS0ExAsEzK3x0kh4V5f5JD9QF0+RSGcCmHarTYpK9EPvgLrDeMJLFe8x6xHg0X\\/IufjagxlHY2+bQazSZRVwtJVtpRV3OCrc18mAgs34v6H5z785UnbpwdwCcipeidFxfaBgwccRb0aZ8sJfDeQAnRoM8lWJXmGR6C7TQZ\\/sMrQaYwQK2E5rrKeYA9NcWZ5Mjm9BYskYAaDeN2FXWKxkrC38Svguse9IrDWMUakUz8wiLqVYjPzC69UtMHOTVI4HNXh0iGCvL7a6cCEhdjyOkSL\\/aeIR9jArMRd8ZjUEbYvPWfqrcPsrE+snETI\\/IcP\\/XBD2wNRagUddt7mU1OCkHfiKPrh0jdOPIbSzNbIXIaW8zmBmWNyX1lhHsgB8JgzH6TOl6MnxDj2ZWc6TUrpKvBt5hmiNgH02pQnKhuVskp7X2k+qom4kw7jZL\\/IJtrw1qaNFu5rCgtpfIoZspGpmglPcvToSsTxECTVIW3bUV1LYPkBVCtjG1PDAVpIhgc7LmonAYzAoGqefMKB1uenXmijfB+eaJswYUo440pOxdMkKAR22VH5BI6xutyfq13V0nmYmNqDsBuB0Gpn+rvQU5ECCUvrVOny70GaD0OOGkvEAKtuztmXcZXje8P992fGrngwQC8KUb6flvb0M1ugVbhg15191zRxOPfspoR2sndGsd2D36W0NzNGX6s63FZ8DOyGfYtlbyWHLwoPzD5nR0KoUOqFG2OI6AbgfzoK5\\/Sg==";
        String encryptedText = "99B+2zm8RefhZPNCi4DiO1\\/PKO7qTeNMeIoUHxXxEW\\/omBtwl27sPHP9rZ053ri2Wr4K3dSgs0xRyfSCTSiyLYujAPhiaBmyzWrCJoeZLVHEACaljkbmeq7h2V1lrpvwZFYBKGpwpy1WJs16Ae\\/YlQAHf31pLxDZxaxLiQu6FmHGHfu+XZvM+XY4+w+ZWx0XX7JkGHKrrXbI+L7D6dCpKy0avqvd8EfGV79t5OELnKjgLDg1nuoHnXHgP+xqwtGGXkBYen0Al1fkS\\/lh917WiG5rfNV\\/C2VGnsjOAtc5hexzSUoMYXLhRZzHv5OHs61NDY48T+pDXoeXZ6fDa4Uq3i9hlgXetKC+q3XdGvxaCWWdhwtxWQLlW7\\/2ZlCzpF2DoyN+tpdHuMUE9UPNu2+oooDqIc7V97hGqS7PhrsxBYSQUq0MrM0qiXt4wVh0563mPvrkQJLXAEEyOAaRXyvlnJytsl6sFTPsz8i8YyS0JLJYDQiVXuJcPqjvXo6t\\/0IlKAe\\/3Xvt0kU99PrDjxp0c8PYyKiJlB5Gq5H4hh3ga3BXWr1GmUbRd1OTRgj03EcqE5z56CYYpiDuMJUxHR8kYxDNbgggQJX0JKZbRdR4x3FtpBvlt2clRja+W42aoiKEaPsJIk696RtGtczg7BxwLtXm\\/HuMvqKFXnatzGXa0ZATcpncgvMihwCbzxd\\/VP4672eirCuYGkeb0+py5TfnQOcJn4qtveeZbTdmvcvOIQOavL5HebqfrVhKFy7qdO\\/Yi\\/tcy8JVW\\/DppEm0SB\\/SauilC7xaLID1yF4LBeqh4eWU4VQRsOc9ZehUT1n7jvTYzi0htGilkenstTjdtXTZSkuH0m6Tlk1xkwy\\/hXl7Fm9n5inG18WpigmUN1aCQpn54lBl\\/6Zd1p9cUpufhcFxsnJCJ3B5mdvRmVKiwQaYKF8weoSG47knY3ab3HW4+sZY7nw3y4DBmS112vpkww9y\\/E5be1TwwZkZtd5CLfMV94ScwuYUtwGIZg5DnaslZWhFLqb57c417\\/\\/Qt34hdqR9qj0OITMnFMHB+L6XgOOiW\\/+Gjy4xXINOMl4bDA8EVS9aGFsX+ZfTg8IBwF33aLa7UZQb8pdllJClABOk4\\/zDG0QZFP1AAJufI94PEfy54irXvXujmoyWy0h278YnrH0CzjPHe84dzIDsmZq+fBfpIw8EEji56pcnJ3yrwDD7jJmQQFZDg7PpXHgSmj2vU9vL2jCYFyGFuH5d8LnA0zJA1raXe1Q+V7sNrVJeL3UnrA1IlSyoQT1P10rzREjWXafT8Wps1chCXkZRgv8nhnBrTQeRt1lkcabL1kAj9dZEasgu2OBA3+rXlrtoyLQvk\\/tPlMfg5nKbQt3eCbRFkhGzwZcJe1k2fqjHejpTOQePQp78rhQjVdZOTPSmucZWbOazEIBu3xNGL2Xqp3UZvFCh9O3MYRFyefSRuL5dbNVZqUUQi2rXaKpK9pf5BI\\/Pc\\/GbtMcHPu+K0wbcP\\/8dAIusOEF4XtQ7YjKZSL6vvwYVJ1UPdO+51qeMqYk7ZJJpUuqdDTp0+B0VO63KZv6HtftQFTuDSyvjOQwRY8WhvWRm\\/cjuEuLW\\/bH\\/12l\\/cxTLWtNZLqYshTgGs7XOc9vjswVaFW0znmeWyPhoM6JSOuBzhr2iJKKLKBcCjkkXwfFCucqgf2q7w3v7TZOjsoyYTvRWH4VbnKysBzw62Gs\\/JfF64tI1X9jEYHgw0Y68eHX8T+4PdTHVCzTwwswkxyWya51Pl3exHjgs8ImYOPaLmgc5KQ6rQnjJHCZ1hyQgmnOeAj9jgEFenHhNNYzxccMys9eoesjfMk1JEfGKCAKzYiuWOHwclbt0mmg6v8jMV6SQsNDVU9RjHG4+KmckiWbdu2cpZBSNE22xdEB8IzUA48F6ezWuswohTB8txKILhGOPmHWbQ2LiQZ\\/bU6A1foV5fytEOTFsBHfIC\\/JNHq3vXw5Xvprre19xJOzptGzrzVlY+DtUTrsVpDpL92m7DyMfcKQ9RvGiLVcuoJdyiHhGSFWfIBD53s9DdcGnSm44EE7cYDb\\/ElVCQoXgrhrHHr0jUZGWQX8OE054OSysPu\\/pv5WU2Og1jbqpfElbsvkBf6WddbpXmP1+G2r2IHNb5w2WAAi3RFE1bxQBPxyhi9RxXndyCR\\/jjD9KzoBFjnyAiK8QM02wPDJ3y6+h6FAoSGh7byH0DIcg0BH+KRU4+Lz3gBKsC0koY31CqDAU7tQpiqd2S8fBA1YWhc3mpQtZXaoWUZKMqNnocQb\\/xor4V2DClJWB\\/Kf5Htc8842LaApcrJIcr94oyiPZxsJPGateMZltcxDv873RCHTSwn8UqXRWSlQIKX6wKhrSaJhNT1De3UDA+ia6Cl6IaBtbKsXRJC1v5s9GJGCDaqejmyUZcfYXxp58fSL\\/JDJgafd4eiPDAOlcYpvVtAdIeNsFJNHx0XKeEjRXEs39fI\\/Na1xvJM9kQ4isKdsZSB\\/VYf8wS5JhbkAZD04aNuDVaufE4nCWQNb+qv7dnHv4hSXYM6bGHGRYxLbhE4iTRGn5xw6mDzV7jvAB+kgKmmN63o9Jf6qCGMtePCiv361UnrhTxb1J31VVbypdFMk1KXOhYYo0Aq8IU3lJrH72SouWRWBOu+2vTL6PFQnhHJx9nTwOdHaFgMNcqj0U4vlpNrs3Q5Ueuu49U976wVQFosozQCi7ZlPAHCTFPRPL76h\\/Lpvmi2XbpaUN0iee\\/es4sZMISyGWZYsoAd0ejZ5z4bjWvv2MVhPQZy83p7KhS5Fg8aRAGsDkdlgZfEfm4hooxN2IVE+cdoIoGgDMyEbzSnzkuTnvO4hHb0eHZq+8BaNIKNQmheQlewO0rwAIhv\\/\\/fqWgluKRZ7cXfP5E8sxbGgugvmiXHJpDKrFUkM6cvo7tAhvkFIF4+ara\\/4iH\\/9s11iFPKyiXmWgbbkU6eCVMkum1sh9xm6p44ws3KU9H+5q7j3Wgzn0s0kJhgB1TJOEyVAMxDYqNls8jeosz6SCFzjq3eZ9w8pWNB7Ub9SWV9Fvr1Vrdp2zn03tqI1sqdyjv9Hk3+LnT+D1Dh\\/XffelTo3o3uDnj+Q0NjKxJLMdRnGyXlN3ThBqRY9P5OHpENtOnSYyQ6km9efD17Cn10LrH9POzDQWpFirvpPL31YQTElzkcl\\/9NKm5FnVU5NyTN62s9rwh3fxJG2q\\/xzKxQNbFEb\\/7JP34y2Z\\/Q7Xv28FTl2dPKYdMq7dB7JZ30\\/ethghvrgP+Abf3AYTHO\\/Dw1gygJC4oTStOqHaTNkvGxu\\/BnZkUS3evl0W1B6BFR+z3GhgeI6EOqd1hN58J7GTTMue40ncbMCVxwWU0xNkdhyV5RcEM5fnvbaMyO5Rz6\\/LPOJa2WNhIOIVqJXDgbWRP+ODmluiit5sdedec3AJZTOrQi0f94YJkINrh1WNppyxHGTLrA51UnOLeIqCWyQlGJsdP+KIlfj4oLwLyl4GxTyJ6Kv1yOdqR54z6KzsRlqoMDr2FCXUrPAWwq8TBkrm9ZO3VZg7RAIAMY3Ps1NyZA6z87M\\/0Qhlv19R6QESy3DA+8mTte3E+YfRau3rHk5b68fh4Ln+UQrIGX78Sfl5EqHeUpw+myfT4FbB5k7bBF+v4LVRI1Ib6QfusCvUjit4Cwp02J2o27FZNydnLwJnnflyp9zy9hOgQI\\/rJyL8YQxTEwylZbzlIq2lBMq4nECk\\/YCGSMaRSWOboO\\/5D5Ufl9QC+i+3m+lqqrgPq17iCObH5k+9UUi+JwipRJ\\/AODPhAm2rZzBlA0CeX7bHkmG+1i35HBT0SXvlKSWaXQs2oUl2EEZhYB6UQ413Sj6GIajQPXkMxPfiDcnstGq1msvZTlzyAjI+miUlgNBCc4qApzkXph6wkMyYPMT0+dzi03GXYKJcowKG6\\/SPqhAwMtMJ1z6HLrJLZgeEtAZl2EzH87neISmSsusWVAQMxE2RSx3cn7KUWRR80rJ\\/YLhiAVkuPkWO2F\\/eY2Nn3Jb0jVcBs8zfuvjK7fT\\/wfEzAMUbhcGq8KzBweTR4HjIBWnYDHITWm1WGBp4og6pNDLI948jWCT6ln9Z4k6bmmo2psG\\/59aX04BGXvItuqHDA3je8krsW3xws1e+bLI+JT7nWgqndQs7yGNDAXp1nuKtghZ4C2f9gF1aq5IKov2uMgVKChco4C5uG4yg\\/vuQWwPdgRiUa4hVY+DSH";
//        String encryptedText = "eSgpbfxEKGfjgaSVBo\\/KJBjqAEynL7CbE6CqUEEWLJ4JUhjd0lAn5oGnFh2IkXGTbqchX6+rjcvVyAbQmQzph5UeF7uYRq0gxOX+590eRuUtFkeLED+GLPiCOsFjSrqczJA2rPnfYJQlMJypDTM0FyI50Q61ysj8dJa9PhHwl6sbcHGT\\/jHVxnnL3KIMz3KVnZJHTRB7yFaafaO4kKHkJ5gCKdviRWYQC04DBQspfgP6xZnloPn2rMWqx0gtgzGGoyYwLStRL6xx5B8O\\/Bhxtl2c77xqXC6\\/bOvM+bcCmthLbIlb16TFutWGGu7zqq5DTkrdC27SmgvpUq9pHZxuJ4NEOHxG34WkTyuegA0yjYgg3j4RsWL0i69LoQ\\/DZNPAeHFO\\/eqnzd+zLaMKFch7CQp\\/WVWAoUcocCt3zOvUDJeDLK4C\\/E+Lp0XdZdD\\/0Z9c03TQIBc3hes71WEx2cW+MiKXZn30VlQt7etIXYsKaGVUW\\/nRUvWRWkrvTkou+eU6q4mHDwHfEWcHVNPNBiN8IUDiHZwZ2zouIYAhLAvZHdLsRNvtZiOwbzYqEXPgEVznDi6lbqdMovtZGfCSA7OKx9BLysvBL8c1U3RojHgXgz7BhgaxAAF4yTtSxPBHy5IED12r\\/dMUvNtBIi4nVGL\\/4qlWsQOniiroIP\\/KY1EwF3WhqocoN3j04GOUNeTXaA3tz0bg0TaUctKcp3ki9G81SV3sQSyikpn4PrzFps701+LUtxV9SuiEAbtoK0YrmB0pmhntIH2UaF80fF+M4ZG5K1KATIu2F6fkfL7DN+foVyukPIMKNUI1jQzi5kQRYBJWkc\\/69dOl9pSp71+gTAYJqi4WDrql56c7yaPV5Ou\\/FiBaYOCN7UkYSuplubReVDxqi\\/YySqBavlS2DEialor6BBGLbOULUnvYkEBLPUFsFQMcvcw2e0F0YdG4QEcrM0wogpFu2NsuVbjuuFR4wtGzWvAxyfrZYSAT7p6sdcaU5SvKxGypoRTOUIGnUSNLesVb2a4iFZykhRz1x64laWXKXLzMV\\/vYq2TWG5R4undOR\\/EHE+M250sFVh3XsQbjO640UIg9FztxKXNfV8C39Zm275mqmVgELwiZH7WPYwgo57VV026HB516JnEajLAtHVTpSFgeReGOCNQWdZUO+tt5v9JE2TGOGO3fRdvK44TYGqVWEkvpILYNul+qwTFMLZMNkrb9PN4sOj4AnUjUepMRLPerFuKL9e35v6+HTBOcBLMLnYl6PA5ljVlTAqLxeVNnH95Prnmm+0SIywHLh2h+NVreiWmmjdzXTxcsFveAGiML3cRhVYBzpp+867Cp7Cp\\/\\/AWE1lFTLWXkzO+i9R2SnNF\\/NlM\\/Y0qi1qQKl3Je1m7tioaBX1DsXijKEdaRYIpo63sAXMigGFObRF7qaGRXHnxZAK8rvNbpM0lljdIIiK2h0MYKo18aR7j4OA7cehD1vr22QoTCswzZ5N0V0Nw9VODufAQUx5JJCn033szTJfZxoL1W55KdQxzvmF43CVbMMkjx7+aodCkKjl673Un2rBwy8159MzJWQbABb\\/ZJw\\/+52fZMGDQhSZWEIFgeqV3kHdXxCmlhgdlqoUfC6gMmgjlfAk2bRr+0QnbN473\\/xur3xIxNsncSDWtSv4wE7mTINHhQURZUIFDn27y8yKR0ou7JmE\\/HxB194SH9aWUuFfBYSsDJZmt+BIz1ruKWVA1LTsOJajIdo1deh1uLtcBfS\\/OenBpWLNWYmbXEOBq4r5BuwIr5gzFMIXxcYe\\/3qc8ojXUJm+K2oCS9GzxmErwPwPZ8HOfSe0k4U2JfDPAGzoGTHbqvH4hBkGEoAjnXcdWr9N9+WD0RsInNdZ670c5yIH04vRtFgTnDn+0tjFQvJGNEwmP4t3Xi8Ydj3P1Fu2OxQjl\\/stbTlyr59CV+TT3Y4gcRPwCc+TbwYuEQPVoOkwcMahBYWX0FDoGgWwtk1mkqRx\\/Q4bThvjXUyDzrf3RKzWB8APfmTAFtazwLNHXVHfYGGqlwQ6C\\/lO90icvETEffqxYgKl8KjB94pUkJ54PalL9Uwz1cZPFfpn2msotWfr53TnJ9rVbA7pozDwVqREPXEBiSpPOCsZ5BhncYiSPErTH2ekGY\\/ZcWhsnRb3bxBMCE\\/qxUrFumX+E4peW5IXak+3VSyfULmi\\/tD3+dMxpR23Tkn35vfpvF6UHexLvqD2\\/uY4c7F3gUfvfr61q6t9XMFWS8060dobgGI3SkcxoXi7UPIJDLLG8dkIKXspx2wzUwCK\\/LwNDu\\/yFBK7pqB9YPRfEkEPDfiLhIaFQ+Jxrzd\\/bFDA9HudTzlN70VtkERozx7ZCP2H\\/vS6UnO7wVSveHT+bNkb\\/mYC8cKRZFUhYmJxI1mY0q1NSqqTOWtfUxMJdXKU38XXCHhYELZFVVmzyEjf+OPUWwqWjHO7RpmkXx3\\/PpF\\/OIGZyi0IZSfeb\\/hiTgJTuWEPxyFbT73kKpMAilYkc746aJZswpzbNM0hM0cAA3MyXLsiNNUVc9u1lN+m1S81t1pV2Ffg5WsNpjs1DVJg33HCs754MebImv206cz+QFSktmN9u1CIopv+5O2pcISoEBPHkpPLJaDemDeQ3+QvvxHFfMHMGbztAmCpDlqpiVWOm391dapqPL72oqpAFtDZrOt18wvyEn9peZ9oeAszNpBtSbJvSZSsi42HFaOQinRx\\/M5zd47\\/8KpmHapLplNJUOT49gpaAIebRC2buVXh9TdKSHpA60naPMKtU5BrVorq89USk6OdL3Ig3vn7ARo9cvhkcUdlxmXR+sZha2NFEBvPHOIx6vkoMfFOQfigRUchEe0pvZdUpOfVodSfaeDb\\/iXceOn+6m0bxGBj6+MPgpUUZU2unPTdNx+Pqck9sB0TM7uIPkja0+V82JYde5kybjrePTc19SYIdsyBx1f\\/ouH9iYa9g3cOFSZ93u\\/a7xGL+b2vD05CDYtS2nYYJSk+6Ys2qLRfUtTqBLkCgLxdbEkVFMv\\/h6H5AIKcVTQ1+lCP9L65c7MQ9hw62Qq+ArlhLnbsZaZcDDBeE8eILQTqiA6TXBvqWwq9HYp4rm\\/HwxCzM39UOTjhe94jIOXU\\/Rz+8P\\/Ng2UkyqaaZ15mMiLlUii\\/5opdQHjcreDXnylaQLBzOgCe754If9D+BOoX44NxHSnnlcthpo92DW2neEt83psowJ2wYDKmabBLx\\/nxY4JqLJqpnC+3uXx6mZPz2QpH5gbc\\/ilxXMWiYsh+vOAEp28kYlNV7fwnN6eJ9cMs+B0dy9l\\/LlGAN8U+6WRgJSvloX7k\\/\\/IrmPywPeVoMq0mO2MC8QHILVCiU2+h3S7PNEo\\/qvhIltOiJT2f8Z89Ao4X2czc3FMOs3i2fAj0zTPD6dDDfpoKF+znUZK\\/5o4GKgPpeoKZZ7EmA2I4JSlKCzHr875wtXAK7Pf4iB6uscEUqjP8y4Mwo69w0CcUHkOwV5vrykdo0LLlE8dkYT+OJbR8\\/nuzdOzr8im\\/lxFU+8d8L5+HkYx6Xk+Wm\\/a2SA9dfs3RrnqFSlpKa71xrguDNei83aEx0CXnc4cj3R2fRtKLvxu0Q5VZOleX4PntVKL1qv1uUS5+KfyrE8y20Krv2BXgsMHJ1A1JgDsU1jSQAgEotLOgBndAlnCamkfesxQex2U9KEC6rkjoNmr1JLqc2INllHqrvq6hW1Te75D7yTG2skFfD7sweiE+uxDloCCQ==";

        byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

        String decryptedText = new String(decryptedTextBytes);
        System.out.println(decryptedText);


    }
}
