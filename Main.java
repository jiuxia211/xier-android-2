import java.time.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*默认开店时间是8:00~22:00,测试时可在MyAnimalShop中的CloseShop中自行修改*/
        Test.test1();    //测试异常InsufficientBalanceException
        Test.test2();    //正常使用
        Test.test3();    //不初始化动物列表，测试AnimalNotFountException
        Test.test4();   //另一种正常使用
    }


}

abstract class Animal {
    protected String AnimalName;
    protected int age;
    protected String gender;
    protected double price;//TODO 将dog和cat的price固定

    public Animal(String AnimalName, int age, String gender, double price) {
        this.AnimalName = AnimalName;
        this.age = age;
        this.gender = gender;
        this.price = price;
    }

    public abstract String toString();
}

class dog extends Animal {
    boolean isVaccineInjected;

    public dog(String AnimalName, int age, String gender, double price, boolean isVaccineInjected) {
        super(AnimalName, age, gender, price);
        this.isVaccineInjected = isVaccineInjected;
    }

    @Override
    public String toString() {
        return "狗狗" + AnimalName + ",性别" + gender + ",它今年" + age + "岁了\n" + "它的价格为" + price
                + '\n' + "它是否打过疫苗？  " + isVaccineInjected;
    }
}

class cat extends Animal {
    public cat(String AnimalName, int age, String gender, double price) {
        super(AnimalName, age, gender, price);
    }

    @Override
    public String toString() {
        return "猫猫" + AnimalName + ",性别" + gender + ",它今年" + age + "岁了\n" + "它的价格为" + price;
    }
}

class ElectronicPet extends Animal {
    double height;

    public ElectronicPet(String AnimalName, int age, String gender, double price, double height) {
        super(AnimalName, age, gender, price);
        this.height = height;
    }

    @Override
    public String toString() {
        return "电子宠物（误）" + AnimalName + ",性别" + gender + ",它今年" + age + "岁了\n" + "它的价格为" + price
                + "\n她的身高为" + height;
    }
}

class Customer {
    public String name;
    public int VisitNumber;
    int year;
    int month;
    int day;
    LocalDate VisitTime;

    public Customer(String name, int year, int month, int day, int VisitNumber) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        VisitTime = LocalDate.of(year, month, day);
        this.VisitNumber = VisitNumber;
    }

    public Customer(String name) {
        this.name = name;
    }

    public String toString() {
        if (VisitTime != null)
            return ("招待了" + "顾客" + name + "顾客" + name + "已经来过" + VisitNumber + "次了" + '\n' + name + "上次来宠物店是在" + VisitTime);
        else
            return ("招待了" + "顾客" + "顾客" + name + "已经来过" + VisitNumber + "次了" + '\n' + name + "之前从未来过");

    }
}

interface AnimalShop {
    void BuyNewAnimal(Animal a);

    void EntertainCustomer(Customer a);

    void CloseShop();
}

class MyAnimalShop implements AnimalShop {

    double RemainingSum;
    double profit;
    public ArrayList<Animal> AnimalList = new ArrayList<>();
    public ArrayList<Customer> CustomerList = new ArrayList<>();

    public static boolean open;

    public MyAnimalShop(double RemainingSum) {
        this.RemainingSum = RemainingSum;

    }

    @Override      //TODO 三个重载的方法均给出从未使用的警告
    public void BuyNewAnimal(Animal a) {
        if (a.price > RemainingSum) {
            throw new InsufficientBalance("InsufficientBalanceException");
        } else {
            AnimalList.add(a);
            RemainingSum -= a.price;
            profit -= a.price;
            System.out.println("已经购买了" + a.AnimalName);
            System.out.println(a);
        }
    }

    @Override
    public void EntertainCustomer(Customer a) {
        CustomerList.add(a);
        a.VisitNumber++;
    }

    public void SaleAnimal(Animal a) {
        if (AnimalList.isEmpty()) {
            throw new FindAnimal("AnimalNotFountException");
        } else {
            System.out.println("已经出售了" + a.AnimalName);
            System.out.println(a);
            RemainingSum += a.price;
            profit += a.price;
            AnimalList.remove(a);
        }
    }

    @Override
    public void CloseShop() {
        LocalTime StartTime = LocalTime.of(8, 0, 0);
        LocalTime EndTime = LocalTime.of(22, 0, 0);
        LocalTime NowTime = LocalTime.now();
        if (NowTime.isAfter(StartTime) && NowTime.isBefore(EndTime)) {
            System.out.println("未到歇业时间!");
        } else {
            System.out.println("歇业成功!");
            open = false;
            System.out.println("现在在宠物店有以下动物");
            for (Object o : AnimalList) {
                System.out.println(o.toString());
            }
            for (Object o : CustomerList) {
                System.out.println(o.toString());
            }
            System.out.println("今天的利润为" + profit + "元");
        }

    }

}

class FindAnimal extends RuntimeException {
    public FindAnimal(String message) {
        super(message);
    }

}

class InsufficientBalance extends RuntimeException {
    public InsufficientBalance(String message) {
        super(message);
    }
}

class Test {
    public static void test1() {
        MyAnimalShop MyAnimalShopA = new MyAnimalShop(1145.14);
        Animal RockSugar = new cat("RockSugar", 3, "female", 200);
        Animal XuanDog = new dog("XuanDog", 2, "male", 250, false);
        MyAnimalShopA.AnimalList.add(RockSugar);
        MyAnimalShopA.AnimalList.add(XuanDog);//初始化动物列表
        Animal JiaRan = new ElectronicPet("嘉然今天吃什么", 8, "female", 10000, 1.08);
        MyAnimalShopA.BuyNewAnimal(JiaRan);//测试异常InsufficientBalanceException
        Customer a = new Customer("a", 2022, 1, 20, 2);
        MyAnimalShopA.EntertainCustomer(a);
        MyAnimalShopA.CloseShop();

    }

    public static void test2() {
        MyAnimalShop MyAnimalShopA = new MyAnimalShop(1145.14);
        Animal RockSugar = new cat("RockSugar", 3, "female", 200);
        Animal XuanDog = new dog("XuanDog", 2, "male", 250, false);
        Animal JiaRan = new ElectronicPet("嘉然今天吃什么", 8, "female", 10000, 1.08);
        MyAnimalShopA.AnimalList.add(RockSugar);
        MyAnimalShopA.AnimalList.add(JiaRan);
        MyAnimalShopA.AnimalList.add(XuanDog);//初始化动物列表
        Animal Azi = new ElectronicPet("阿梓从小就很可爱", 18, "female", 20, 1.63);
        MyAnimalShopA.BuyNewAnimal(Azi);
        MyAnimalShopA.SaleAnimal(XuanDog);
        Customer a = new Customer("a", 2022, 1, 20, 2);
        MyAnimalShopA.EntertainCustomer(a);
        Customer b = new Customer("b", 2021, 6, 1, 1);
        MyAnimalShopA.EntertainCustomer(b);
        Customer c = new Customer("c");
        MyAnimalShopA.EntertainCustomer(c);
        MyAnimalShopA.CloseShop();

    }

    public static void test3() {
        MyAnimalShop MyAnimalShopA = new MyAnimalShop(1145.14);
        Animal JiaRan = new ElectronicPet("嘉然今天吃什么", 8, "female", 10000, 1.08);
        //不初始化动物列表，测试AnimalNotFountException
        MyAnimalShopA.SaleAnimal(JiaRan);
    }

    public static void test4() {
        MyAnimalShop MyAnimalShopA = new MyAnimalShop(1145.14);
        Animal RockSugar = new cat("RockSugar", 3, "female", 200);
        Animal XuanDog = new dog("XuanDog", 2, "male", 250, false);
        Animal JiaRan = new ElectronicPet("嘉然今天吃什么", 8, "female", 10000, 1.08);
        Animal Dark = new dog("Dark", 0, "male", 100, true);
        MyAnimalShopA.AnimalList.add(RockSugar);
        MyAnimalShopA.AnimalList.add(XuanDog);
        MyAnimalShopA.AnimalList.add(JiaRan);
        MyAnimalShopA.AnimalList.add(Dark);//初始化动物列表
        Animal ycyc = new cat("ycyc", 2, "male", 200);
        MyAnimalShopA.BuyNewAnimal(ycyc);
        MyAnimalShopA.SaleAnimal(Dark);
        Customer a = new Customer("a", 2022, 1, 20, 2);
        MyAnimalShopA.EntertainCustomer(a);
        Customer b = new Customer("b", 2021, 6, 1, 1);
        MyAnimalShopA.EntertainCustomer(b);
        Customer c = new Customer("c");
        MyAnimalShopA.EntertainCustomer(c);
        MyAnimalShopA.CloseShop();

    }


}

