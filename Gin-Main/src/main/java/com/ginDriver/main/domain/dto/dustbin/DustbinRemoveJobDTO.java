package com.ginDriver.main.domain.dto.dustbin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DustbinRemoveJobDTO extends DustbinRemoveDTO{
    private LocalDateTime removeTime;
}
