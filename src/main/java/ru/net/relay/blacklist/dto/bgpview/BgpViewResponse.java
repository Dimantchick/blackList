package ru.net.relay.blacklist.dto.bgpview;

public record BgpViewResponse(
        String status,
        BgpViewData data
) {

}
