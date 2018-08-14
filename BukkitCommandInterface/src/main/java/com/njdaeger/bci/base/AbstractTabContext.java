package com.njdaeger.bci.base;

import com.njdaeger.bci.SenderType;
import com.njdaeger.bci.base.executors.TabExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractTabContext<C extends AbstractCommandContext, T extends AbstractTabContext> {
    
    private final List<String> possible;
    private final BCICommand<C, T> command;
    private final C commandContext;
    private final String[] args;
    
    public AbstractTabContext(C commandContext, BCICommand<C, T> command) {
        this.possible = new ArrayList<>();
        this.commandContext = commandContext;
        this.args = commandContext.args;
        this.command = command;
    }
    
    public boolean hasPermission(String permission) {
        return commandContext.hasPermission(permission);
    }
    
    public boolean hasAnyPermission(String... permissions) {
        return commandContext.hasAnyPermission(permissions);
    }
    
    public boolean hasAllPermissions(String... permissions) {
        return commandContext.hasAllPermissions(permissions);
    }
    
    public String getAlias() {
        return commandContext.getAlias();
    }
    
    public CommandSender getSender() {
        return commandContext.getSender();
    }
    
    public C getCommandContext() {
        return commandContext;
    }

    public BCICommand<C, T> getCommand() {
        return command;
    }
    
    public int getLength() {
        return args.length;
    }
    
    public boolean isLength(int length) {
        return getLength() == length;
    }
    
    public String getPrevious() {
        return getLength() == 1 ? null : args[getLength()-2];
    }
    
    //can be regex
    public boolean isPrevious(String previous) {
        return getPrevious() != null && getPrevious().matches(previous);
    }
    
    //cant be regex
    public boolean isPrevious(boolean ignoreCase, String previous) {
        return getPrevious() != null && (ignoreCase ? getPrevious().equalsIgnoreCase(previous) : getPrevious().equals(previous));
    }
    
    public String getCurrent() {
        return getLength() == 0 ? null : args[getLength()-1];
    }
    
    //can be regex
    public boolean isCurrent(String current) {
        return getCurrent() != null && getCurrent().matches(current);
    }
    
    //cant be regex
    public boolean isCurrent(boolean ignoreCase, String current) {
        return getCurrent() != null && (ignoreCase ? getCurrent().equalsIgnoreCase(current) : getCurrent().equals(current));
    }
    
    public String argAt(int index) {
        return (index > -1 && index < getLength()) ? args[index] : null;
    }
    
    public List<String> getArgs() {
        return Arrays.asList(args);
    }
    
    public boolean hasArgs() {
        return getLength() > 0;
    }
    
    public void playerCompletion() {
        possible.clear();
        possible.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }
    
    public void playerCompletionAt(int index) {
        if (isLength(index + 1)) playerCompletion();
    }
    
    //can be regex
    public void playerCompletionAfter(String previousArg) {
        if (isPrevious(previousArg)) playerCompletion();
    }
    
    //cant be regex
    public void playerCompletionAfter(boolean ignoreCase, String previousArg) {
        if (isPrevious(ignoreCase, previousArg)) playerCompletion();
    }
    
    public void playerCompletionIf(Predicate<T> predicate) {
        if (predicate.test((T)this)) playerCompletion();
    }
    
    public void completion(String... completions) {
        possible.clear();
        possible.addAll(Arrays.asList(completions));
    }
    
    public void completionAt(int index, String... completions) {
        if (isLength(index + 1)) completion(completions);
    }
    
    //can be regex
    public void completionAfter(String previousArg, String... completions) {
        if (isPrevious(previousArg)) completion(completions);
    }
    
    //cant be regex
    public void completionAfter(boolean ignoreCase, String previousArg, String... completions) {
        if (isPrevious(ignoreCase, previousArg)) completion(completions);
    }
    
    public void completionIf(Predicate<T> predicate, String... completions) {
        if (predicate.test((T)this)) completion(completions);
    }
    
    public void completion(Function<T, List<String>> function) {
        possible.clear();
        possible.addAll(function.apply((T)this));
    }
    
    public void completionAt(int index, Function<T, List<String>> function) {
        if (isLength(index + 1)) completion(function);
    }
    
    public void completionAfter(String previousArg, Function<T, List<String>> function) {
        if (isPrevious(previousArg)) completion(function);
    }
    
    public void completionAfter(boolean ignoreCase, String previousArg, Function<T, List<String>> function) {
        if (isPrevious(ignoreCase, previousArg)) completion(function);
    }
    
    public void completionIf(Predicate<T> predicate, Function<T, List<String>> function) {
        if (predicate.test((T)this)) completion(function);
    }
    
    public void subCompletion(TabExecutor<T> executor) throws BCIException {
        executor.complete((T)this);
    }
    
    public boolean subCompletion(SenderType senderType, TabExecutor<T> executor) throws BCIException {
        if (SenderType.of(getSender()).equals(senderType)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }
    
    public boolean subCompletionAt(int index, TabExecutor<T> executor) throws BCIException {
        if (isLength(index + 1)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }
    
    public boolean subCompletionAt(int index, SenderType senderType, TabExecutor<T> executor) throws BCIException {
        if (isLength(index + 1)) {
            return subCompletion(senderType, executor);
        }
        return false;
    }
    
    public boolean subCompletionAt(int index, String match, TabExecutor<T> executor) throws BCIException {
        return subCompletionAt(index, false, match, executor);
    }
    
    public boolean subCompletionAt(int index, boolean ignoreCase, String match, TabExecutor<T> executor) throws BCIException {
        if (isLength(index + 1) && isPrevious(ignoreCase, match)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }
    
    public boolean subCompletionIf(Predicate<T> predicate, TabExecutor<T> executor) throws BCIException {
        if (predicate.test((T)this)) {
            subCompletion(executor);
            return true;
        }
        return false;
    }
    
    List<String> currentPossibleCompletions() {
        return possible;
    }
    
}
