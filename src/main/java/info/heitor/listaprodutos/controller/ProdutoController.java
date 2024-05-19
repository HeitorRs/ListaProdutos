package info.heitor.listaprodutos.controller;

import info.heitor.listaprodutos.model.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private List<Produto> produtoList = new ArrayList<>();
    private int proxId = 4;

    public ProdutoController() {
        produtoList.addAll(List.of(
                new Produto(1,"Maça", 2.50),
                new Produto(2,"Banana",12.00),
                new Produto(3,"Limão",8.00)
        ));
    }

    @GetMapping
    public List<Produto> listaProdutos(){
        return produtoList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoPorId(@PathVariable int id) {
        Optional<Produto> produto = produtoList.stream().filter(p -> p.getId() == id).findFirst();
        return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProduto(@RequestBody Produto produto) {
        if (produto.getNome() == null || produto.getNome().isEmpty() || produto.getValor() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        produto.setId(proxId++);
        produtoList.add(produto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable int id, @RequestBody Produto produto) {
        Optional<Produto> existeProduto = produtoList.stream().filter(p -> p.getId() == id).findFirst();
        if (existeProduto.isPresent()) {
            Produto updatedProduto = existeProduto.get();
            updatedProduto.setNome(produto.getNome());
            updatedProduto.setValor(produto.getValor());
            return ResponseEntity.ok(updatedProduto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable int id) {
        boolean removido = produtoList.removeIf(p -> p.getId() == id);
        return removido? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
