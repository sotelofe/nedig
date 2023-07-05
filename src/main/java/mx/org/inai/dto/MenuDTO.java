package mx.org.inai.dto;

public class MenuDTO {
	
    private Integer idMenu;
    private Integer idPerfil;
    private String menu;
    private String menuNav;
    private String descMenu;
    private String estatus;
	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public Integer getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getMenuNav() {
		return menuNav;
	}
	public void setMenuNav(String menuNav) {
		this.menuNav = menuNav;
	}
	public String getDescMenu() {
		return descMenu;
	}
	public void setDescMenu(String descMenu) {
		this.descMenu = descMenu;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
    
    
}
