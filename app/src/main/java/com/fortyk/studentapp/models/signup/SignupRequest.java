package com.fortyk.studentapp.models.signup;

import java.util.List;

public class SignupRequest {
  private String curriculumid;
  private  String schoolname;
  private  String standard;
  private List<Students> students;

  public SignupRequest(String curriculumid, String schoolname, String standard, List<Students> students) {
    this.curriculumid = curriculumid;
    this.schoolname = schoolname;
    this.standard = standard;
    this.students = students;
  }

  public String getCurriculumid() {
    return curriculumid;
  }

  public void setCurriculumid(String curriculumid) {
    this.curriculumid = curriculumid;
  }

  public String getSchoolname() {
    return schoolname;
  }

  public void setSchoolname(String schoolname) {
    this.schoolname = schoolname;
  }

  public String getStandard() {
    return standard;
  }

  public void setStandard(String standard) {
    this.standard = standard;
  }

  public List<Students> getStudents() {
    return students;
  }

  public void setStudents(List<Students> students) {
    this.students = students;
  }
}
