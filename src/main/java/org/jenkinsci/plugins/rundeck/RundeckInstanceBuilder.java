package org.jenkinsci.plugins.rundeck;

import hudson.util.Secret;
import org.jenkinsci.plugins.rundeck.client.RundeckClientManager;
import org.jenkinsci.plugins.rundeck.client.RundeckManager;

public class RundeckInstanceBuilder {
    private String name;
    private String url;
    private int apiVersion=RundeckClientManager.API_VERSION;
    private String login;
    private Secret token;
    private Secret password;
    RundeckManager client;

    public RundeckInstanceBuilder() {
    }

    void setClient(RundeckManager client){
        this.client = client;
    }

    RundeckManager getClient(){
        return client;
    }

    RundeckInstanceBuilder client(RundeckManager client) throws IllegalArgumentException {
        if(client.getRundeckInstance().getName()!=null) {
            this.name = client.getRundeckInstance().getName();
        }

        this.url = client.getRundeckInstance().getUrl();

        // Token and username-password are mutually exclusive: Prefer Token
        if(client.getRundeckInstance().getToken()!=null){
            this.token = client.getRundeckInstance().getToken();
        } else {
            if(client.getRundeckInstance().getPassword()!=null && client.getRundeckInstance().getLogin()!=null){
                this.password = client.getRundeckInstance().getPassword();
                this.login = client.getRundeckInstance().getLogin();
            } else {
                throw new IllegalArgumentException("Rundeck instance must have either a token or or both login and password");
            }
        }

        if(client.getRundeckInstance().getApiVersion()!=null){
            this.apiVersion = client.getRundeckInstance().getApiVersion();
        }else{
            this.apiVersion = RundeckClientManager.API_VERSION;
        }

        return this;
    }

    RundeckInstanceBuilder client(RundeckInstance client) throws IllegalArgumentException{
        this.url = client.getUrl();
        if(client.getName()!=null) {
            this.name = client.getName();
        }
        if(client.getToken()!=null){
            this.token = client.getToken();
            } else {
                if(client.getPassword()!=null && client.getLogin()!=null){
                this.password = client.getPassword();
                this.login = client.getLogin();
            } else {
                throw new IllegalArgumentException("Rundeck instance must have either a token or both login and password");
            }
        }

        this.apiVersion = client.getApiVersion();
        return this;
    }

    RundeckInstanceBuilder url(String url){
        this.url = url;
        return this;
    }

    RundeckInstanceBuilder version(int apiVersion){
        this.apiVersion = apiVersion;
        return this;
    }

    RundeckInstanceBuilder login(String login, Secret password){
        this.login = login;
        this.password = password;
        return this;
    }

    RundeckInstanceBuilder token(Secret token){
        this.token = token;
        return this;
    }

    RundeckInstanceBuilder name(String name){
        this.name = name;
        return this;
    }

    public RundeckInstance build() {
        RundeckInstance client = new RundeckInstance(this.name, this.url);
        client.setApiVersion(this.apiVersion);
        if (this.token != null) {
            client.setToken(this.token);
        } else {
            client.setLogin(this.login);
            client.setPassword(this.password);
        }

        return client;
    }


    static RundeckClientManager createClient(RundeckInstance instance){
        RundeckClientManager clientManager = new RundeckClientManager(instance);
        return clientManager;
    }

    @Override
    public String toString() {
        return "RundeckInstanceBuilder{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", apiVersion=" + apiVersion +
                ", login='" + login + '\'' +
                ", token=" + token +
                ", password=" + password +
                ", client=" + client +
                '}';
    }
}
