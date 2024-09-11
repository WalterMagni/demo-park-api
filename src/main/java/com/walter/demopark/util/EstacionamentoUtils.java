package com.walter.demopark.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    public static String gerarRecibo()  {
        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);

        //20240906-15:54:11

        return recibo
                .replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }

}
