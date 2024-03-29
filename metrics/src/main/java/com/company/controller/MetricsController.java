package com.company.controller;

import com.company.dto.DateQueryDto;
import com.company.dto.LogDto;
import com.company.dto.MetricsDto;
import com.company.dto.RegionDto;
import com.company.service.MetricsService;
import com.company.util.DtoCreator;
import com.company.util.LogFile;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LoggerConfig;
import org.bson.Document;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/laa")
@Produces(MediaType.APPLICATION_JSON)
public class MetricsController {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
        LoggerConfig.of(logger).addHandler(LogFile.getLogFile());
    }

    @GET
    @Path("/metrics")
    public MetricsDto getMetrics(
            @NotNull @QueryParam("day") String day,
            @NotNull @QueryParam("week") String weekYear,
            @NotNull @QueryParam("year") String year
    ) {
        logger.atInfo().log("Endpoint GET /laa/metrics received a request");
        List<LogDto> mostAccessedUrls = metricsService.findMostAccessedUrls(3);
        List<RegionDto> mostAccessedUrlsPerRegion = metricsService.findMostAccessedUrlsPerRegion(3);
        List<LogDto> lessAccessedUrls = metricsService.findLessAccessedUrls(1);
        List<DateQueryDto> mostAccessedUrlsPerDates = metricsService.findMostAccessedUrlsPerDates(day, weekYear, year, 3);
        Document minuteWithMoreAccess = metricsService.findMinuteWithMoreAccess();

        MetricsDto metricsDto = DtoCreator.metricsDto(
                mostAccessedUrls,
                mostAccessedUrlsPerRegion,
                lessAccessedUrls,
                mostAccessedUrlsPerDates,
                minuteWithMoreAccess);

        return metricsDto;
    }

    @GET
    @Path("/metrics/1")
    public List<LogDto> getMostAccessedUrls() {
        logger.atInfo().log("Endpoint GET /laa/metrics/1 received a request");
        return metricsService.findMostAccessedUrls(3);
    }

    @GET
    @Path("/metrics/2")
    public List<RegionDto> getMostAccessedUrlsPerRegion() {
        logger.atInfo().log("Endpoint GET /laa/metrics/2 received a request");
        return metricsService.findMostAccessedUrlsPerRegion(3);
    }

    @GET
    @Path("/metrics/3")
    public List<LogDto> getLessAccessedUrls() {
        logger.atInfo().log("Endpoint GET /laa/metrics/3 received a request");
        return metricsService.findLessAccessedUrls(1);
    }

    @GET
    @Path("/metrics/4")
    public List<DateQueryDto> getDateMetrics(
            @QueryParam("day") String day, @QueryParam("week") String weekYear, @QueryParam("year") String year
    ) {
        logger.atInfo().log("Endpoint GET /laa/metrics/4 received a request");
        return metricsService.findMostAccessedUrlsPerDates(day, weekYear, year, 3);
    }

    @GET
    @Path("/metrics/5")
    public Document getMostAccessedMinute() {
        logger.atInfo().log("Endpoint GET /laa/metrics/5 received a request");
        return metricsService.findMinuteWithMoreAccess();
    }
}
