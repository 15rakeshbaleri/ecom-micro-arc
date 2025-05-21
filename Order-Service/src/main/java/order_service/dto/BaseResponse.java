package order_service.dto;

import jakarta.persistence.Temporal;
import lombok.Data;

import java.util.Date;
import java.util.Map;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Data
public class BaseResponse {
    public String Status;
    public String messageType;
    public String message;
    public Date timeStamp;
    public Object data;  // This should be a generic object to hold any payload
}

