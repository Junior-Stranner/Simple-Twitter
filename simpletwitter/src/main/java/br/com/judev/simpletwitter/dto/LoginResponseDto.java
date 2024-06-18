package br.com.judev.simpletwitter.dto;

public class LoginResponseDto {
    private String acessToken;
    private long expireIn;

    public LoginResponseDto() {
    }



    public String getAcessToken() {
        return acessToken;
    }

    public void setAcessToken(String acessToken) {
        this.acessToken = acessToken;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }
}
