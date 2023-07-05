package mx.org.inai.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MENU")
public class Menu {
	
	@Id
	@GeneratedValue(generator="menu_idmenu_seq")
	@Column(name="idmenu")
    private Integer idMenu;
	
	@Column(name="idperfil")
    private Integer idPerfil;
	
	@Column(name="menu")
    private String menu;
	
	@Column(name="menunav")
    private String menuNav;
	
	@Column(name="descmenu")
    private String descMenu;
	
	@Column(name="estatus")
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
