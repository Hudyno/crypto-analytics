package com.crypto.analytics.util;

import com.crypto.cryptocompare.api.min.response.HistoricalOHLCV;
import com.crypto.cryptocompare.api.min.response.HistoricalOHLCVItem;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CryptoCompareMinUtil {

    public void removePreTradeEntries(HistoricalOHLCV historicalOHLCV) {
        List<HistoricalOHLCVItem> itemList = historicalOHLCV.getHistoricalOHLCVItemList();

        int debutItemIndex = itemList.stream()
                                     .filter(this::isDebutTradingDay)
                                     .map(itemList::indexOf)
                                     .findFirst()
                                     .orElse(-1);

        if (debutItemIndex > 0) {
            itemList.subList(0, debutItemIndex).clear();
        } else if (debutItemIndex == -1) {
            itemList.clear();
        }
    }

    public boolean isDebutTradingDay(HistoricalOHLCVItem item) {
        if (isNull(item.getOpen())) {
            item.setOpen(0.0);
        }
        if (isNull(item.getClose())) {
            item.setClose(0.0);
        }
        if (isNull(item.getHigh())) {
            item.setHigh(0.0);
        }
        if (isNull(item.getLow())) {
            item.setLow(0.0);
        }

        return item.getOpen() != 0.0 || item.getClose() != 0.0 || item.getHigh() != 0.0 || item.getLow() != 0.0;
    }
}
