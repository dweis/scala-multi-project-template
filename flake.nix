{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
    systems.url = "github:nix-systems/default";
    # Dev tools
    treefmt-nix.url = "github:numtide/treefmt-nix";
  };

  outputs = { self, flake-parts, nixpkgs, ... }@ inputs:
    inputs.flake-parts.lib.mkFlake { inherit inputs; } {
      systems = import inputs.systems;
      imports = [
        inputs.treefmt-nix.flakeModule
      ];
      perSystem = { config, self', pkgs, lib, system, nixpkgs, ... }:
        let
          jdk = pkgs.zulu21; #graalvm-ce; # pkgs.graalvmCEPackages.graalvm11-ce-bare; # graalvm-ce;
          sbt = pkgs.sbt.override { jre = jdk; };
          buildInputs = [ ];
          nativeBuildInputs = [
            jdk
            sbt
          ];
        in
        {
          devShells.default = pkgs.mkShell {
            inputsFrom = [
              config.treefmt.build.devShell
            ];
            shellHook = ''
              export JAVA_HOME=${jdk}/
              export PATH="$JAVA_HOME/bin:$PATH"
              echo
              echo "Run 'just <recipe>' to get started 🚀🚀🚀"
              just
            '';

            inherit buildInputs;
            nativeBuildInputs = nativeBuildInputs ++ (with pkgs; [
              ammonite
              coursier
              dive
              jmeter
              just
              metals
              scalafmt
            ]);
          };

          treefmt.config = {
            projectRootFile = "flake.nix";
            programs = {
              nixpkgs-fmt.enable = true;
              scalafmt.enable = true;
            };
          };
        };
    };
}
