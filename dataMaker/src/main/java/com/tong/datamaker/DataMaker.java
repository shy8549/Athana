package com.tong.datamaker;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

public class DataMaker {

    private static final Logger log = Logger.getLogger(com.tong.datamaker.DataMaker.class);

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            int totalRows = Integer.parseInt(cmd.getOptionValue("rows"));
            int numberOfThreads = Integer.parseInt(cmd.getOptionValue("threads"));
            String outputFileName = cmd.getOptionValue("file");
            String scheduleOption = cmd.getOptionValue("schedule");
            String xdrNameOption = cmd.getOptionValue("xdr_name");

            // 运行程序逻辑
            log.info("Total Rows: " + totalRows);
            log.info("Number of Threads: " + numberOfThreads);
            log.info("Output File: " + outputFileName);
            log.info("Output schedule: " + scheduleOption);
            log.info("Output File: " + xdrNameOption);

            long startTime = System.currentTimeMillis();

            long endTime = System.currentTimeMillis();
            log.info(" Data generation and write to csv completed. Time taken: " + (endTime - startTime) + " ms");

        } catch (ParseException e) {
            log.info(e.getMessage());
            new HelpFormatter().printHelp("utility-name", options);

            System.exit(1);
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        Option rowsOption = new Option("r", "rows", true, "Total number of rows to generate");
        rowsOption.setRequired(true);
        options.addOption(rowsOption);

        Option threadsOption = new Option("t", "threads", true, "Number of threads to use");
        threadsOption.setRequired(true);
        options.addOption(threadsOption);

        Option fileOption = new Option("f", "file", true, "Output file name");
        fileOption.setRequired(true);
        options.addOption(fileOption);

        Option scheduleOption = new Option("s", "schedule", true, "Print data generation progress ");
        scheduleOption.setRequired(true);
        options.addOption(fileOption);

        Option xdrOption = new Option("x", "xdr_name", true, "choose xdr name like (http,gen) ");
        xdrOption.setRequired(true);
        options.addOption(fileOption);

        return options;
    }
}
