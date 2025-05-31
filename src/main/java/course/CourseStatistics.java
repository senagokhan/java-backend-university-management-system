package course;

public class CourseStatistics {
    private int courseId;
    private double lowestGrade;
    private double highestGrade;
    private double averageGrade;

    public CourseStatistics(){

    }

    public CourseStatistics(int courseId, double lowestGrade, double highestGrade, double averageGrade) {
        this.courseId = courseId;
        this.lowestGrade = lowestGrade;
        this.highestGrade = highestGrade;
        this.averageGrade = averageGrade;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public double getLowestGrade() {
        return lowestGrade;
    }

    public void setLowestGrade(double lowestGrade) {
        this.lowestGrade = lowestGrade;
    }

    public double getAvarageGrade() {
        return averageGrade;
    }

    public void setAvarageGrade(double sum,int num) {
        this.averageGrade = Math.round((sum/num)* 100.0) / 100.0;
    }

    public double getHighestGrade() {
        return highestGrade;
    }

    public void setHighestGrade(double highestGrade) {
        this.highestGrade = highestGrade;
    }
}
