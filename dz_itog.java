import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, 
//разделенные пробелом:
// Фамилия Имя Отчество датарождения номертелефона пол
// Форматы данных:
// фамилия, имя, отчество - строки
// датарождения - строка формата dd.mm.yyyy
// номертелефона - целое беззнаковое число без форматирования
// пол - символ латиницей f или m.
// Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, 
//вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, 
//чем требуется.
// Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. 
//Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать 
//встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение
//с информацией, что именно неверно.
// Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку 
//должны записаться полученные данные, вида
// <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
// Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
// Не забудьте закрыть соединение с файлом.
// При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.


public class dz_itog {
    public static void main(String[] args) {  
        
        Scanner sc = new Scanner (System.in);
        System.out.println("Введите в одну строку через пробел Фамилию_Имя_Отчество_Дату рождения(дд.мм.гггг)_Номер телефона(11 цифр)_Пол человека(m or w)");
        String resStr = sc.nextLine();
        String [] res = resStr.split("[ //s]+");
        System.out.println(Arrays.toString(res));

        try {
            if (res.length == 6){
                Human man = new Human();
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    if (res[0].chars().allMatch(Character::isLetter)){
                        man.surname = res[0];
                        stringBuilder.append(man.surname);
                    }
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }
                try {
                    if (res[1].chars().allMatch(Character::isLetter)){
                        man.name = res[1];
                        stringBuilder.append(man.name);
                    }
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }
                try {
                    if (res[2].chars().allMatch(Character::isLetter)){
                        man.pftronymic = res[2];
                        stringBuilder.append(man.pftronymic);
                    }
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }    
                String [] Bd = man.birthday.split("[.//s]+");
                System.out.println(Arrays.toString(Bd));
                try {
                    if (Bd.length == 3 && Bd[0].length() == 2 && Bd[1].length() == 2 && Bd[2].length() == 4){
                        int a = Integer.parseInt(Bd[0]);
                        int b = Integer.parseInt(Bd[1]);
                        int c = Integer.parseInt(Bd[2]);
                        if ((a >= 1 && a <= 31) && (b >= 1 && b <= 12) && (c >= 1900 && c  <= 2023)){
                            man.birthday = res[3];
                            stringBuilder.append(man.birthday);
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }
                try {
                    man.number = Integer.parseInt(res[4]);
                    stringBuilder.append(man.number);
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }
                try {
                    if (res[5].chars().allMatch(Character::isLetter)){
                        if (res[5] == "m" || res[5] == "w"){
                            man.gender = res[5].charAt(0);
                            stringBuilder.append(man.gender);
                        } 
                    }
                } catch (NumberFormatException e) {
                    throw new EnteringCharactersinExplanationException();
                    System.out.println(e.getMessage());
                }
                String result = stringBuilder.toString();
                System.out.println(result);
                String fileName = man.getSurname();
                
                try {
                    w_data(fileName, result);
                    r_data(fileName);
                } catch (FileDoesNotWriteException | FileDoesNotExistException e) {
                    System.out.println(e.getMessage());
                }
            }  
        } catch (NumberFormatException e) {
            throw new IncorrectAmountOfDataException();
          }
        sc.close();
    
    }
    
        public static void w_data(String fileName, String text) throws IOException {
            fileName = fileName + ".txt";
            try (FileWriter fileWriter = new FileWriter( fileName, true)){
                fileWriter.write(text);
            } catch (IOException e) {
            System.out.println(e.getMessage());
            }
        

        }



    
        public static void r_data(String fileName) throws IOException{
            try(FileReader fileReader = new FileReader(fileName + ".txt")){
                while (fileReader.ready()) {
                    System.out.print((char) fileReader.read());
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
            

}  


class FileDoesNotExistException extends FileNotFoundException {
    public FileDoesNotExistException(Exception e) {
        super("По указанному пути файл:   не найден");
        e.printStackTrace();
    }
    
}

class FileDoesNotWriteException extends IOException {
    public FileDoesNotWriteException(Exception e) {
        super("При записи файла по указанному пути:   возникла ошибка");
        e.printStackTrace();
    }
    
}

class IncorrectAmountOfDataException extends IOException {
    public IncorrectAmountOfDataException() {
        super("Веедено неверное количество данных. Вы ввели большее или меньшее количество параметров. Проверьте ввод");}
    
}

class EnteringCharactersinExplanationException extends IOException {
    public EnteringCharactersinExplanationException() {
        super("Вееденное значение содержит посторонние символы");}
    
}

class Human{
    String surname;
    String name;
    String pftronymic;
    String birthday;
    Integer number;
    public Character gender;
    

    public Human(String surname, String name, String pftronymic, String birthday, Integer number, Character gender){
        this.surname = surname;
        this.name = name;
        this.pftronymic = pftronymic;
        this.birthday = birthday;
        this.number = number;
        this.gender = gender;
    }

    public Human (){

    }

    public String getSurname(){
        return surname;
    }
}