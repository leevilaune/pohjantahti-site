package dev.onesnzeroes.pt.web;

import dev.onesnzeroes.pt.domain.TagUtil;
import dev.onesnzeroes.pt.service.ClanDtos.ClanDetail;
import dev.onesnzeroes.pt.service.ClanDtos.ClanSummary;
import dev.onesnzeroes.pt.service.ClanDtos.MemberDto;
import dev.onesnzeroes.pt.service.ClanDtos.Overview;
import dev.onesnzeroes.pt.service.ClanService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Read-only API for the website. Tags can be given with or without '#', e.g.
 * {@code /api/clans/2GY2L0UQP}.
 */
@RestController
@RequestMapping("/api")
public class ClanController {

    private final ClanService service;

    public ClanController(ClanService service) {
        this.service = service;
    }

    /** Totals across all tracked clans plus a summary of each. */
    @GetMapping("/overview")
    public Overview overview() {
        return service.overview();
    }

    @GetMapping("/clans")
    public List<ClanSummary> clans() {
        return service.listClans();
    }

    @GetMapping("/clans/{tag}")
    public ClanDetail clan(@PathVariable String tag) {
        return service.getClan(TagUtil.requireValid(tag));
    }

    @GetMapping("/clans/{tag}/members")
    public List<MemberDto> members(@PathVariable String tag) {
        return service.listMembers(TagUtil.requireValid(tag));
    }
}
