package team.j2e8.findcateserver.infrastructure.usercontext;



public interface IdentityContext {

    Object getIdentity();

    boolean isAuthenticated();

    String getAuthKey();
}
