# MinhaLoja

App Android (Kotlin + XML) com login no **Firebase Authentication**, lista de produtos da **Fake Store API** via **Retrofit** e favoritos salvos no **Cloud Firestore**.

## Integrantes

- Murilo Francisco Alvim
- _(adicione os demais integrantes aqui)_

## Funcionalidades

- Login e cadastro com e-mail e senha, com mensagens de erro claras
- Usuário já logado abre direto na lista de produtos
- Lista de produtos (`GET /products`) em `RecyclerView` com título, preço, categoria e foto (Coil)
- Aviso de carregamento e mensagem de erro com "Tentar novamente"
- Tocar em um produto salva em `usuarios/{uid}/favoritos/{idProduto}` com `salvoEm` (timestamp do servidor)
- Tela "Meus favoritos" ordenada do mais recente para o mais antigo, com remoção
- Botão "Sair"

## Como rodar

1. Clone o repositório e abra a pasta no Android Studio.
2. Crie um projeto no [Console do Firebase](https://console.firebase.google.com) e adicione um app Android com o pacote `com.murilo.minhaloja`.
3. Baixe o `google-services.json` e coloque em `app/` (ele não está no repositório por segurança).
4. No Firebase, ative **Authentication → E-mail/senha** e crie o **Firestore Database** (modo de teste).
5. Rode o app em um emulador ou celular (Run ▶).

## Estrutura

```
app/src/main/java/com/murilo/minhaloja/
├── model/        Produto, Rating, Favorito (data classes)
├── network/      ProdutoService (interface Retrofit) e RetrofitClient
├── data/         FavoritosRepository (Firestore)
├── adapter/      ProdutoAdapter e FavoritoAdapter (RecyclerView)
├── ui/           LoginActivity, ProdutosActivity, FavoritosActivity
└── util/         Formatadores (preço e data)
```

`firestore.rules` tem as regras de segurança (desafio extra): cada usuário só lê e grava em `usuarios/{seu uid}`.
