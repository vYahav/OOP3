package tests;





public class Cat {
    private int age,hoursCleaningFloors;
    private String name;
    private boolean peeOnFloor;
    private  int kaki_size;

    public Cat(int age) {
        this.age = age;
        this.hoursCleaningFloors=0;
        this.peeOnFloor = false;
        int kaki_size = 0; //size is 0,1,2
        name="";
    }
public void Kaki(int size){
      this.kaki_size = size;
    }
public int GetKaki(){
        return this.kaki_size;
}
    public Cat(int age , String  name) {
        this.age = age;
        this.hoursCleaningFloors=0;
        this.peeOnFloor = false;
        this.name = name;
    }


    public void notTakenForAWalk(int hours) {
        if(hours>8&& this.age > 5){
            this.peeOnFloor=true;
            hoursCleaningFloors=0;
        }
    }


    public void hoursCleaningFloors(int hours) {
        if(this.peeOnFloor){
            this.hoursCleaningFloors=this.hoursCleaningFloors+hours;
        }
        if(this.hoursCleaningFloors>10){
            this.peeOnFloor=false;
            this.hoursCleaningFloors=0;
        }
    }

    public String houseCondition() {
        if(this.peeOnFloor){
            return "smelly";
        }
        return "clean";
    }
}
