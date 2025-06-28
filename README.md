# 🌤 Aplicativo de Previsão do Tempo e Fase Lunar

Uma aplicação Android que exibe previsões do tempo, fase lunar e histórico local das últimas consultas. Desenvolvido em Java, com Material Components, Google Play Services Location, Room (SQLite) e Retrofit.

---

## 📖 Sumário

* [Funcionalidades](#funcionalidades)
* [Instalação](#instalação)
* [Uso](#uso)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Dependências](#dependências)
* [Localização](#localização)
* [Contribuição](#contribuição)
* [Licença](#licença)

---

## 🚀 Funcionalidades

* **Localização automática:** captura coordenadas GPS via Fused Location Provider.
* **Dados do tempo:** obtém temperatura, descrição e ícone usando a WeatherAPI.
* **Fase lunar:** mostra a fase atual da lua com ícone e descrição.
* **Histórico local:** persiste as últimas 5 consultas no SQLite (Room) e exibe em RecyclerView.
* **Barra de ferramentas Material:**

  * **Atualizar** (🔄) para refazer a consulta de clima
  * **FAQ** (❓) abre tela de Perguntas Frequentes
  * **Sobre** (ℹ️) abre tela Sobre o App
* **Edge‑to‑Edge** e tema Dia/Noite.
* **Internacionalização:** strings em Português, Inglês e Espanhol.

---

## 📦 Instalação

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/trabalhofinal.git
   ```
2. Abra no Android Studio.
3. Adicione sua chave da WeatherAPI em `WeatherRepository` (ou use mecanismo seguro).
4. Sincronize o Gradle e execute em dispositivo/emulador com GPS ativo.

---

## 🎯 Uso

1. Conceda permissão de localização quando solicitado.
2. Toque no ícone de **Atualizar** para buscar previsão e fase lunar.
3. Role para baixo para ver o **histórico** das últimas 5 consultas.
4. Acesse **FAQ** ou **Sobre** pelo menu da toolbar.

---

## 📂 Estrutura do Projeto

```
app/
 ├─ src/main/
 │   ├─ java/br/com/trabalhofinal/ui/main/
 │   │   ├─ MainActivity.java
 │   │   ├─ SobreActivity.java
 │   │   └─ DuvidasActivity.java
 │   ├─ java/br/com/trabalhofinal/data/
 │   │   ├─ model/        # Modelos de API (WeatherResponse, Results)
 │   │   ├─ repository/   # WeatherRepository (Retrofit)
 │   │   └─ local/
 │   │       ├─ database/ # Configuração do Room
 │   │       └─ model/    # Entidade WeatherHistory
 │   └─ res/
 │       ├─ layout/       # activity_main.xml, activity_sobre.xml, activity_duvidas.xml
 │       ├─ menu/         # top_app_bar_menu.xml
 │       ├─ values/       # strings.xml (pt, en, es), themes, colors
 │       └─ drawable/     # ícones e assets de clima/fase lunar
 └─ build.gradle
```

---

## 🛠️ Dependências

* Material Components
* Google Play Services: Location
* Retrofit & Gson
* Room (SQLite)
* AndroidX (AppCompat, RecyclerView, ConstraintLayout, etc.)

---

## 🌐 Localização

Strings em:

* `res/values/` (Português)
* `res/values-en/` (Inglês)
* `res/values-es/` (Espanhol)

O app detecta o idioma do dispositivo automaticamente.
