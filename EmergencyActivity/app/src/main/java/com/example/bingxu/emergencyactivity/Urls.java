package com.example.bingxu.emergencyactivity;

/**
 * Created by bingxu on 24/08/15.
 */
public class Urls {
    //user login username & password
    // public address 52.27.184.210
    // temporary address 10.201.9.133
    public static final String url = "http://10.201.9.133:8080/EmergencySystem/";
    public static final String urlP = "http://52.27.184.210:8080/EmergencySystem/";
    public static final String urlLogin = "http://52.27.184.210:8080/EmergencySystem/login?";
    public static final String testLogin = url+"login?";

    public static final String test = "http://10.201.9.172:8080/EmergencySystem/getAllDoctor?";

    public static final String urlDoctor = "http://52.27.184.210:8080/EmergencySystem/getAllDoctor?";
    public static final String urlParamedic = "http://52.27.184.210:8080/EmergencySystem/getAllParamedic?";
    public static final String urlTestLogin = urlP+"login?";
    public static final String urlCreateUser = urlP+"createUser?";
    public static final String urlGetInfo = urlP+"getUserInfo?";

    public static final String urlRoster = "http://52.27.184.210:8080/EmergencySystem/getRoster?";
    public static final String urlFetchRoster = "http://52.27.184.210:8080/EmergencySystem/getUserRoster?";
    public static final String urlEditRoster = "http://52.27.184.210:8080/EmergencySystem/editRoster?";
    //http://52.27.184.210:8080/EmergencySystem/getUserRoster?schedule.year=2015&schedule.month=9&schedule.day=17&schedule.startTime=0&schedule.endTime=8
    public static final String urlActivity = url + "getActivityRecord?";
    public static final String urlSendActivity = url + "insertActivityRecord?";

    public static final String urlJoinActivity = urlP + "acceptInvite?";
    public static final String urlActivityList = urlP + "getActivityList?";
    public static final String urlAddActivity = urlP + "insertActivity?";
    public static final String urlPatientID = urlP + "getPatient?";
    public static final String urlPatientList = urlP + "getPatientList?";
    public static final String urlEditPatientInfo = urlP + "updatePatient?";
    public static final String urlAddPatientInfo = urlP + "insertPatient?";
    public static final String urlAddPatientDes = urlP + "insertPatientRecord?";
    public static final String urlSearchStaff = urlP + "searchStaffList?";


}
