package simulator.launcher;

import org.apache.commons.cli.*;
import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.*;
import simulator.view.MainWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static Integer _timeLimitDefaultValue = 10;
    private static String _mode = "gui";
    private static String _inFile = null;
    private static String _outFile = null;
    private static Integer _timeLimit = null;
    private static Factory<Event> _eventsFactory = null;

    private static void parseArgs(String[] args) {

        // define the valid command line options
        //
        Options cmdLineOptions = buildOptions();

        // parse the command line as provided in args
        //
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(cmdLineOptions, args);
            parseHelpOption(line, cmdLineOptions);
            parseModeOption(line);
            parseInFileOption(line);
            parseOutFileOption(line);
            parseTickOption(line);

            // if there are some remaining arguments, then something wrong is
            // provided in the command line!
            //
            String[] remaining = line.getArgs();
            if (remaining.length > 0) {
                String error = "Illegal arguments:";
                for (String o : remaining)
                    error += (" " + o);
                throw new ParseException(error);
            }

        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }

    }

    private static Options buildOptions() {
        Options cmdLineOptions = new Options();

        cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Graphic mode").build());
        cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
        cmdLineOptions.addOption(
                Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
        cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
        cmdLineOptions.addOption(
                Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10).").build());
        return cmdLineOptions;
    }

    private static void parseModeOption(CommandLine line) throws ParseException {
        if(line.hasOption("m")){
            String s = line.getOptionValue("m");
            if(s == "console"){
                _mode = "console";
            }
            else if(s == "gui"){
                _mode = "gui";
            }
            else
                throw new ParseException("Mode parameter is missing");
        }
    }

    private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
            System.exit(0);
        }
    }

    private static void parseInFileOption(CommandLine line) throws ParseException {
        _inFile = line.getOptionValue("i");
        if (_inFile == null) {
            throw new ParseException("An events file is missing");
        }
    }

    private static void parseOutFileOption(CommandLine line) {
        _outFile = line.getOptionValue("o");
    }

    private static void parseTickOption(CommandLine line) {
        String in = line.getOptionValue("t");
        if(in != null){
            _timeLimit = Integer.parseInt(in);
        }
        else
            _timeLimit = _timeLimitDefaultValue;
    }

    private static void initFactories() {
        List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
        lsbs.add( new RoundRobinStrategyBuilder() );
        lsbs.add( new MostCrowdedStrategyBuilder() );
        Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);

        List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
        dqbs.add( new MoveFirstStrategyBuilder() );
        dqbs.add( new MoveAllStrategyBuilder() );
        Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);

        //Builders list
        List<Builder<Event>> eventList = new ArrayList<>();
        eventList.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
        eventList.add(new NewCityRoadEventBuilder());
        eventList.add(new NewInterCityRoadEventBuilder());
        eventList.add(new NewVehicleEventBuilder());
        eventList.add(new SetContClassEventBuilder());
        eventList.add(new SetWeatherEventBuilder());

        _eventsFactory = new BuilderBasedFactory<>(eventList);

    }

    private static void startBatchMode() throws Exception {
    	
    	TrafficSimulator simulator = new TrafficSimulator();
    	Controller controller = new Controller (simulator, _eventsFactory);
    	InputStream input = new FileInputStream (new File(_inFile));
    	OutputStream outStr;
    	
    	if (_outFile != null) {
    		outStr = new FileOutputStream (new File(_inFile));
    	}
    	else
    	    outStr = System.out;

    	try {
            controller.loadEvents(input);
            controller.run(_timeLimit);
        }catch (Exception e){
    	    e.printStackTrace();
        }

    }

    private static void startGUIMode() throws Exception{
        TrafficSimulator simulator = new TrafficSimulator();
        Controller controller = new Controller(simulator, _eventsFactory);
        InputStream input = new FileInputStream (new File(_inFile));

        try{
            controller.loadEvents(input);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainWindow(controller);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void start(String[] args) {
        initFactories();
        parseArgs(args);
        try {
            switch (_mode){
                case "gui":
                    startGUIMode();
                    break;
                case "console":
                    startBatchMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // example command lines:
    //
    // -i resources/examples/ex1.json
    // -i resources/examples/ex1.json -t 300
    // -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
    // --help

    public static void main(String[] args) {
        try {
            start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
