package getir.reading.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class OrderDetailsIntervalRequest {
    @NotBlank(message = "start time cannot be blank")
    @NotNull(message = "start time cannot be null")
    String startTime;
    @NotBlank(message = "start time cannot be blank")
    @NotNull(message = "start time cannot be null")
    String endTime;
}
