package accountService.environment;

import accountService.model.*;

import java.time.DayOfWeek;
import java.util.*;

public class Generator {
    private static final Random RAND = new Random();
    private static int id = 0;


    public static User generateUser(int vacationDaysPerYear, Group group){
        User user = generateUser();
        user.setVacationDaysPerYear(vacationDaysPerYear);
        user.setGroup(group);
        id++;
        return user;
    }



    public static User generateUser(){
        final User user = new User();
        user.setFirstName("FirstName" + id);
        user.setLastName("LastName" + id);
        user.setUsername("username" + id + "@holiday.cz");
        user.setPassword(Integer.toString(id));
        user.setPosition("IT_GOAT");
        user.setNonWorkingDay(new ArrayList<DayOfWeek>());
        user.setTeam(generateTeam());
        user.getTeam().addUser(user);
        id++;
        return user;
    }


    public static VacationDay generateVacationDay(User user){
        VacationDay vacationDay = new VacationDay();
        vacationDay.setVacationTaker(user);
        vacationDay.setReason(VacationReason.FAMILY);
        return vacationDay;
    }

    public static Group generateGroup(int[] employeesPerDay){
        final Group group = new Group();
        group.setName("Group" + RAND.nextInt());
        group.setEmployeesPerDay(Generator.generateEmployeesPerDay(employeesPerDay));
        return group;
    }

    public static Group generateGroup(){
        final Group group = new Group();
        group.setName("Group" + RAND.nextInt());
        return group;
    }



    public static Team generateTeam() {
        Team team = new Team();
        team.setName("Test Team" + RAND.nextInt());
        team.setClosestDayLimit(0);
        team.setFurthestDayLimit(0);

        Map<VacationReason, VacationPriority> map = new HashMap();
        map.put(VacationReason.HOLIDAY, VacationPriority.LOW);
        map.put(VacationReason.HOSPITAL, VacationPriority.HIGH);
        map.put(VacationReason.FAMILY, VacationPriority.MEDIUM);
        team.setReasons(map);

        return team;

    }

    public static HashMap<DayOfWeek, Integer> generateEmployeesPerDay(int[] array){
        HashMap<DayOfWeek, Integer> map = new HashMap<DayOfWeek, Integer>();
        map.put(DayOfWeek.MONDAY, array[0]);
        map.put(DayOfWeek.TUESDAY, array[1]);
        map.put(DayOfWeek.WEDNESDAY, array[2]);
        map.put(DayOfWeek.THURSDAY, array[3]);
        map.put(DayOfWeek.FRIDAY, array[4]);
        map.put(DayOfWeek.SATURDAY, array[5]);
        map.put(DayOfWeek.SUNDAY, array[6]);
        return map;
    }
}
