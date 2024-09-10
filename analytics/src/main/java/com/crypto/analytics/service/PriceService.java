package com.crypto.analytics.service;

import com.crypto.persistence.dto.TimeClosingPriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class PriceService {

    public static void synchronizeClosingPriceIntervals(List<TimeClosingPriceDto> xAllTimeClosingPrice, List<TimeClosingPriceDto> yAllTimeClosingPrice) {

        List<TimeClosingPriceDto> mainAllTimeClosingPrice = xAllTimeClosingPrice.size() >= yAllTimeClosingPrice.size() ? xAllTimeClosingPrice : yAllTimeClosingPrice;
        List<TimeClosingPriceDto> subAllTimeClosingPrice = xAllTimeClosingPrice.size() < yAllTimeClosingPrice.size() ? xAllTimeClosingPrice : yAllTimeClosingPrice;

        ZonedDateTime mainStartTime = mainAllTimeClosingPrice.get(0).getTime();
        ZonedDateTime mainEndTime = mainAllTimeClosingPrice.get(mainAllTimeClosingPrice.size() - 1).getTime();

        ZonedDateTime subStartTime = subAllTimeClosingPrice.get(0).getTime();
        ZonedDateTime subEndTime = subAllTimeClosingPrice.get(subAllTimeClosingPrice.size() - 1).getTime();

        if (subStartTime.isBefore(mainStartTime) || subEndTime.isAfter(mainEndTime)) {
            log.info("Main historical prices do not fully contain interval of sub historical prices");
            return;
        }

        mainAllTimeClosingPrice.removeIf(it -> it.getTime().isBefore(subStartTime) || it.getTime().isAfter(subEndTime));
    }
}
