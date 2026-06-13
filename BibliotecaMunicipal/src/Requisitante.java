public class Requisitante {

    private int id;
    private String nome;
    private String contacto;
    private String localidade;
    private String codigo_postal;
    private String pais;

    public Requisitante(int id, String nome, String contacto, String localidade, String codigo_postal, String pais) {
        this.id = id;
        this.nome = nome;
        this.contacto = contacto;
        this.localidade = localidade;
        this.codigo_postal = codigo_postal;
        this.pais = pais;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getContacto() { return contacto; }
    public String getLocalidade() { return localidade; }
    public String getCodigo_postal() { return codigo_postal; }
    public String getPais() { return  pais; }

    public void setNome(String nome) { this.nome = nome; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setCodigo_postal(String codigo_postal) { this.codigo_postal = codigo_postal; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Tel: %s | %s | %s | %s", this.id, this.nome, this.contacto, this.localidade, this.codigo_postal, this.pais);
    }
}