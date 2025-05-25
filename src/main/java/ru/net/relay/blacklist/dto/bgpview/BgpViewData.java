package ru.net.relay.blacklist.dto.bgpview;

import java.util.List;

public record BgpViewData(
        List<BgpViewPrefixes> ipv4_prefixes
) {
}
