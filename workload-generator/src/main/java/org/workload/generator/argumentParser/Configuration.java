package org.workload.generator.argumentParser;

public class Configuration {

    private Mode mode;
    private String url;
    private Integer threadNumber;
    private Boolean virtualThread;
    private String requestMethod;
    private Integer requestNumber;
    private static final Mode DEFAULT_MODE = Mode.oneShot;
    private static final Integer DEFAULT_THREAD_NUMBER = 10;
    private static final Boolean DEFAULT_VIRTUAL_THREAD = false;
    private static final Integer DEFAULT_NUMBER_OF_REQUESTS = 2000;
    private static final String DEFAULT_REQUEST_METHOD = "GET";
    private static final String INIT_THREADS_ERROR_MSG = "Exactly one option regarding threads type should be chosen.";

    private Configuration(Mode mode, String url, Integer threadNumber, Boolean virtualThreads, String requestMethod,
                          Integer requestNumber) {
        this.mode = mode;
        this.url = url;
        this.threadNumber = threadNumber;
        this.virtualThread = virtualThreads;
        this.requestMethod = requestMethod;
        this.requestNumber = requestNumber;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(Integer threadNumber) {
        this.threadNumber = threadNumber;
    }

    public Boolean getVirtualThread() {
        return virtualThread;
    }

    public void setVirtualThread(Boolean virtualThread) {
        this.virtualThread = virtualThread;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public static Configuration parseArguments(String[] args) {
        Mode mode = null;
        String url = null;
        Boolean virtualThreads = null;
        Integer threadsNumber = null;
        Integer requestNumber = null;
        String requestMethod = null;

        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "--requestNumber":
                    assertNotInit(requestNumber, "Request number is duplicated.");
                    assertNextArgument(args, i, "Missing request number value.");
                    try {
                        requestNumber = Integer.valueOf(args[i]);
                    } catch (NumberFormatException ex) {
                        throw new RuntimeException("Request number is not valid number: " + args[i]);
                    }
                    break;
                case "--m":
                case "--mode":
                    assertNotInit(mode, "Mode argument is duplicated.");
                    assertNextArgument(args, i, "Missing mode value.");
                    try {
                        mode = Mode.valueOf(args[++i]);
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalArgumentException("Unknown mode value.");
                    }
                    break;
                case "--vt":
                case "--virtualThread":
                    assertNotInit(virtualThreads, INIT_THREADS_ERROR_MSG);
                    virtualThreads = true;
                    break;
                case "--t":
                case "--thread":
                    assertNotInit(virtualThreads, INIT_THREADS_ERROR_MSG);
                    virtualThreads = false;
                    break;
                case "--tn":
                case "--threadsNumber":
                    assertNotInit(threadsNumber, "Threads number is duplicated.");
                    assertNextArgument(args, i, "Missing threads number value.");
                    try {
                        threadsNumber = Integer.valueOf(args[++i]);
                    }
                    catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Not valid number of threads");
                    }
                    break;
                case "--url":
                    assertNextArgument(args, i, "Missing url value.");
                    url = args[++i];
                    break;
                case "--r":
                case "--requestMethod":
                    assertNotInit(requestMethod, "Request method argument is duplicated.");
                    requestMethod = args[++i].toUpperCase();
                    if (!requestMethod.equals("DELETE") && !requestMethod.equals("POST")
                            && !requestMethod.equals("PUT") && !requestMethod.equals("GET")) {
                        throw new RuntimeException("Not valid request method: " + requestMethod);
                    }
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Unknown argument %s", args[i]));
            }
        }

        if (url == null) {
            throw new IllegalArgumentException("Url is missing.");
        }

        /**
         * Default values for unset values
         * */
        if (mode == null) {
            mode = DEFAULT_MODE;
        }

        if (virtualThreads == null) {
            virtualThreads = DEFAULT_VIRTUAL_THREAD;
        }

        if (threadsNumber == null) {
            threadsNumber = DEFAULT_THREAD_NUMBER;
        }

        if (requestMethod == null) {
            requestMethod = DEFAULT_REQUEST_METHOD;
        }

        if (requestNumber == null) {
            requestNumber = DEFAULT_NUMBER_OF_REQUESTS;
        }

        return new Configuration(mode, url, threadsNumber, virtualThreads, requestMethod, requestNumber);
    }

    private static <T> void assertNotInit(T obj, String msg) {
        if (obj != null) {
            throw new RuntimeException(msg);
        }
    }

    private static void assertNextArgument(String[] args, int currentIdx, String msg) {
        if (args.length <= currentIdx + 1) {
            throw new IllegalArgumentException(msg);
        }
    }
}
