package com.idata.mq.base.message;

public class CommandMessage extends BaseMessage {

    private String command;

    private String[] parameters;

    public CommandMessage() {
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";command:").append(command).append(";parameters:").append(parameters);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }
}
