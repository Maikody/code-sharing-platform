package platform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
public class Code implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private final String code;
    private final LocalDateTime date;
    @JsonIgnore
    private final String uuid = UUID.randomUUID().toString();
    private int views;
    private int time;
    @JsonIgnore
    private LocalTime durationTime;
    @JsonIgnore
    private boolean isRestricted;
    @JsonIgnore
    private boolean isViewRestricted;
    @JsonIgnore
    private boolean isTimeRestricted;

    public Code() {
        this.code = "";
        this.date = LocalDateTime.now();
    }

    public Code(String code) {
        this.code = code;
        this.date = LocalDateTime.now();
    }

    public Code(String code, int views, int seconds) {
        this.code = code;
        this.date = LocalDateTime.now();
        this.views = views;
        this.time = seconds;
        this.durationTime = LocalTime.now().plusSeconds(seconds);
        this.isViewRestricted = views != 0;
        this.isTimeRestricted = seconds != 0;
        this.isRestricted = isTimeRestricted || isViewRestricted;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDate() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return date.format(FORMATTER);
    }

    public String getUUID() {
        return uuid;
    }

    public int getViews() {
        return views;
    }

    public int getTime() {
        return time;
    }

    public LocalTime getDurationTime() {
        return durationTime;
    }

    public boolean getIsRestricted() {
        return isRestricted;
    }

    public boolean getIsViewRestricted() {
        return isViewRestricted;
    }

    public boolean getIsTimeRestricted() {
        return isTimeRestricted;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = LocalTime.now().plusSeconds(durationTime);
    }

    public void setRestricted() {
        if(!isRestricted) {
            this.isRestricted = isTimeRestricted || isViewRestricted;
        }
    }

    public void setViewRestricted() {
        if(!isViewRestricted)
            isViewRestricted = views > 0;
    }

    public void setTimeRestricted() {
        if(!isTimeRestricted)
            isTimeRestricted = durationTime.toSecondOfDay() - LocalTime.now().toSecondOfDay() > 0;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
