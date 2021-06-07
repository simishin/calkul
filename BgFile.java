package qwr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BgFile {
    public  static boolean  prnq(String s){System.out.println(s); return true;}
    public  static boolean  prnt(String s){System.out.print(s); return true;}
    //--------------------------------------------------------------------------
    public  static  int  typz;//тип цифр арабские(123)=1, римские(I,II,V)=2, пока не определились =0, ошибка=-1

    public static boolean readconsol(){
        assert prnq("readconsol");
        prnq("Для завершения работы каркулятора введите <q>");
        int rez=0;  //результат вычислений
        int act=0;  //действие
        try(BufferedReader br = new BufferedReader (new InputStreamReader(System.in)))
        {
            // чтение построчно
            String strIn;
            while(!(strIn=br.readLine()).equals("q")){
                if (strIn.length()<1){ prnq("~"); continue; }
                assert prnq(strIn);
                char    cb=32;
                int     temp=0;
                boolean znak=false; //знак операнда
                int     opA=0; //первый операнд
                String  opsA=""; //первый операнд
                for (int j = 0; j < strIn.length(); j++) {
                    char c = strIn.charAt(j);
                    if ((c<32 || c==32 && cb==c)) continue; //обхожу непечатные символы
                    cb=c;
                    switch (c){
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            if (typz==0 || typz==1) {
                                typz=1;
                                temp=(c-'0')+temp*10;
                            } else typz=-1;
                            break;
                        case 'I':
                        case 'V':
                        case 'X':
                            if (typz==0 || typz==2) {
                                typz=2;
                                opsA=opsA.concat(String.valueOf(c));
                            }
                            else typz=-1;
                            break;
                        case '-': if (typz==0 || act!=0 ) { znak=true; break;}
                                  act++;
                        case '+': act++;
                        case '*': act++;
                        case '/': act++;
                            if (typz==2) {
                                opA=convert(opsA);
                                opsA="";
                            } else {
                                opA=temp; //сохраняю ореранд
                                temp=0;
                            }
                            if (znak) {
                                znak=false;
                                opA=-opA;
                            }
                            break;
                        default: prnq("-@-"); typz=-1;
                    }//switch

                    if (typz<0){ return true; }// соблюдение требования 5
                }//for по символам строки
                if (typz==2) {
                    temp=convert(opsA);

                    rez=operations(opA,znak?-temp:temp,act);
                    prnq(" ="+convert(rez));
                } else {
                    rez=operations(opA,znak?-temp:temp,act);
                    prnq(" ="+rez);
                }
                act=0;
                typz=0;
            }//for input string
            prnq("Quit program");
            return false;
        }
        catch(IOException ex){ System.out.println(ex.getMessage()); }
        return true;
    }//readconsol

    public static int   operations(int opA, int opB, int dev){
        if (opA>10) opA=10; //соблюдение условия 3
        if (opB>10) opB=10; //соблюдение условия 3
        if (opA<1) opA=1;   //соблюдение условия 3
        if (opB<1) opB=1;   //соблюдение условия 3
        switch (dev){
            case 1: if (opB ==0 || opA==0 ) return 0;
                    return opA/opB;
            case 2: return opA*opB;
            case 3: return opA+opB;
            case 4: return opA-opB;
            default: prnq("Error operations!"); return 0;
        }
    }//operations

    public  static  String convert(int opr){//преобразование   числа в строку
        assert opr<200 :"Превышение размерности для перобразования";
        if (opr==0) return "-";
        String rez="";
        int ost=opr;
        if (ost<0){ rez=rez.concat("(-)"); ost=-opr;}
        if (ost>=100) {
            ost-=100;
            rez=rez.concat("C");
        }
        if (ost>=90) {
            ost-=90;
            rez=rez.concat("XC");
        }
        if (ost>=50) {
            ost-=50;
            rez=rez.concat("L");
        }
        if (ost>=40) {
            ost-=40;
            rez=rez.concat("XL");
        }
        while (ost >= 10 ){ rez=rez.concat("X"); ost-=10;}
        if (ost>=9) {
            ost-=9;
            rez=rez.concat("IX");
        }
        if (ost>=5) {
            ost-=5;
            rez=rez.concat("V");
        }
        if (ost>=4) {
            ost-=4;
            rez=rez.concat("IV");
        }
        while (ost-- >0 ){ rez=rez.concat("I"); }
        return rez;
    }//convert

    public  static  int convert(String str){//преобразование части строки в число
        if (str.isBlank()) return 0;
        int i=str.length()-1;
        int rez=0;
        while (str.charAt(i)==' ' && i>0 ) i--;
        while (str.charAt(i)=='I' ) {rez++; i--; if (i <0) return rez;}
        if (str.charAt(i)=='V' ) { rez+=5;i--; if (i <0) return rez;}
        if (str.charAt(i)=='X' ) { rez+=10;i--; if (i <0) return rez;}
        if (str.charAt(i)=='I' ) { rez--;i--; if (i <0) return rez;}
        while (str.charAt(i)=='X' ) {rez+=10; i--; if (i <0) return rez;}
        if (str.charAt(i)=='L' ) { rez+=50;i--; if (i <0) return rez;}
        if (str.charAt(i)=='C' ) { rez+=100;i--; if (i <0) return rez;}
        if (str.charAt(i)=='X' ) { rez-=10;i--; if (i <0) return rez;}
        return -1;
    }//convert

}//class BgFile
