package Model;

public class Permiso {
    private int idPermiso;
    private boolean verModulo;
    private boolean pView;
    private boolean pCreate;
    private boolean pEdit;
    private boolean pDelete;
    private int idRol;
    private int idModulo;
    
    private Modulo modulo;

    public Permiso() {
    }

    public Permiso(int idPermiso, boolean verModulo, boolean pView, boolean pCreate, boolean pEdit, boolean pDelete, int idRol, int idModulo, Modulo modulo) {
        this.idPermiso = idPermiso;
        this.verModulo = verModulo;
        this.pView = pView;
        this.pCreate = pCreate;
        this.pEdit = pEdit;
        this.pDelete = pDelete;
        this.idRol = idRol;
        this.idModulo = idModulo;
        this.modulo = modulo;
    }

    public int getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso = idPermiso;
    }

    public boolean isVerModulo() {
        return verModulo;
    }

    public void setVerModulo(boolean verModulo) {
        this.verModulo = verModulo;
    }

    public boolean isPView() {
        return pView;
    }

    public void setPView(boolean pView) {
        this.pView = pView;
    }

    public boolean isPCreate() {
        return pCreate;
    }

    public void setPCreate(boolean pCreate) {
        this.pCreate = pCreate;
    }

    public boolean isPEdit() {
        return pEdit;
    }

    public void setPEdit(boolean pEdit) {
        this.pEdit = pEdit;
    }

    public boolean isPDelete() {
        return pDelete;
    }

    public void setPDelete(boolean pDelete) {
        this.pDelete = pDelete;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }
}
