package DataClass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;

public class userData implements Serializable {
private String name="user";
private String email="dont@sk.me";
private String address="empty";
private String phone ="N/D";
private int firstTime=1;
private int selectedTemplate=0;

    int[] monthStats=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }
    public int getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(int selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }

    public userData userData(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        return this;
    }

    public void updateDayStats(int amount){
        int today=LocalDate.now().getDayOfMonth()-1;
        monthStats[today]=amount;
        checkAndReset(today);
   }
    public void updateDayStats(){
        int today=LocalDate.now().getDayOfMonth()-1;
        monthStats[today]+=1;
        checkAndReset(today);
    }
   // @JsonIgnore
   @JsonIgnore
   public boolean isfirstLaunch(){
        if(firstTime==1){
            firstTime=0;
        return true;}
        return false;
    }

    @JsonIgnore
    public void checkAndReset(int today){
    for (int i = today+1; i <monthStats.length ; i++) {
        monthStats[i]=0;
    }
}
    @JsonIgnore
    public int getMonthlyStat(){

        int total=0;
        for (int i:monthStats
                 ) {
            System.out.println(i);
                total+=i;
         }
        return total;

        }

    public void setMonthStats(int[] monthStats) {
        this.monthStats = monthStats;
    }
    @JsonIgnore
    public int getTodayStat(){
        return  monthStats[LocalDate.now().getDayOfMonth()-1];
    }
    public int[] getMonthStats() {
        return monthStats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



}

