{
  description = "Minecraft 1.8.9 Modding DevShell";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};

      lwjglLibs = with pkgs; [
        libXxf86vm
        libXcursor
        libXrandr
        libXinerama
        libGL
      ];
    in {
      devShells.${system}.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk8
          gradle
        ] ++ lwjglLibs;

        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath lwjglLibs;

        # optional, aber hilft bei Wayland
        _JAVA_AWT_WM_NONREPARENTING = "1";
      };
    };
}
