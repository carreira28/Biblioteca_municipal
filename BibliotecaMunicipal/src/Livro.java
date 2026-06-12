public class Livro {

    private int id;
    private String isbn;
    private String titulo;
    private int anoPublicacao;
    private String categoria;
    private String idioma;
    private String editora;
    private String genero;
    private String estado;

    public Livro(int id, String isbn, String titulo, int anoPublicacao, String categoria, String idioma, String editora, String genero, String estado) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.categoria = categoria;
        this.idioma = idioma;
        this.editora = editora;
        this.genero = genero;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public String getCategoria() { return categoria; }
    public String getIdioma() { return idioma; }
    public String getEditora() { return editora; }
    public String getGenero() { return genero; }
    public  String getEstado() {
        return estado;
    }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    @Override
    public String toString() {
        return String.format("[%d] %-40s | ISBN: %-15s | Ano: %d | %s | %s | %s |%s", this.id, this.titulo, this.isbn, this.anoPublicacao, this.categoria, this.genero, this.idioma, this.estado);
    }
}