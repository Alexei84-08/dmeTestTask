package com.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static final int INN = 1;
    private static final int KPP = 2;

    private static final String[] STATES = {
            "Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату",
            "Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату",
            "Налогоплательщик зарегистрирован в ЕГРН",
            "Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан",
            "Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН",
            "Некорректный ИНН",
            "Недопустимое количество символов ИНН",
            "Недопустимое количество символов КПП",
            "Недопустимые символы в ИНН",
            "Недопустимые символы в КПП",
            "некорректный формат даты",
            "некорректная дата (ранее 01.01.1991 или позднее текущей даты)",
    };

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String data = formatter.format(LocalDate.now());
        String inn = validateInput(INN);
        String kpp = validateInput(KPP);
        NdsRequest2.NP np = new NdsRequest2.NP();
        np.setINN(inn);
        np.setKPP(kpp);
        np.setDT(data);
        NdsRequest2 nr = new NdsRequest2();
        nr.getNP().add(np);
        FNSNDSCAWS2 fns = new FNSNDSCAWS2();
        FNSNDSCAWS2Port fnaws = fns.getFNSNDSCAWS2Port();
        NdsResponse2 response = fnaws.ndsRequest2(nr);
        for (NdsResponse2.NP p : response.getNP()) {
            System.out.printf("ИНН:%s  КПП:%s  Дата:%s %s", p.getINN(), p.getKPP(), p.getDT(), "\n");
            System.out.printf(" %s", STATES[Integer.parseInt(p.getState())]);
        }
    }

    private static String dataInput(String query) {
        System.out.println(query);
        return scanner.nextLine();
    }

    private static String validateInput(int key) {
        boolean valid = false;
        String result = "";
        while (!valid) {
            if (key == 1) {
                result = dataInput("Введите ИНН: ");
                if (result.matches("([0-9]){1,15}")) {
                    valid = true;
                } else {
                    System.out.println("Не корректное значение ИНН!");
                }
            }
            if (key == 2) {
                result = dataInput("Введите КПП: ");
                if (result.matches("([0-9]){1,15}")) {
                    valid = true;
                } else {
                    System.out.println("Не корректное значение КПП!");
                }
            }
        }
        return result;
    }
}