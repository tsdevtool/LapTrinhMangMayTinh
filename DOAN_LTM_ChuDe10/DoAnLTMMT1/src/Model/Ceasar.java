/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

public class Ceasar {

    char charEnc(char c, int k) {
        if (Character.isLetter(c)) {
            char base = Character.isLowerCase(c) ? 'a' : 'A'; 
            if(Character.isLowerCase(c)){
                return (char) ((((c - base) + k) % 26 + 26) % 26 + base);
            }
            return (char) ((((Character.toUpperCase(c) - base) + k) % 26 + 26) % 26 + base);
        }
        return c; // Không thay đổi kí tự không phải là chữ cái
    }

    public String Encrypt(String br, int k) {
        String kq = "";
        int n = br.length();
        for (int i = 0; i < n; i++) {
            kq += charEnc(br.charAt(i), k);
        }
        return kq;
    }
}
