{
  description = "Tower Defense Java Game";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        jdk = pkgs.openjdk17-bootstrap;
      in
      {
        packages = {
          default = self.packages.${system}.towerdefense;
          
          towerdefense = pkgs.stdenv.mkDerivation {
            pname = "towerdefense";
            version = "0.1.0";
            src = ./.;
            
            buildInputs = [ jdk ];
            
            buildPhase = ''
              mkdir -p $out/bin
              ${jdk}/bin/javac TowerDefenseMain.java
              ${jdk}/bin/jar cfe towerdefense.jar TowerDefenseMain *.class
            '';
            
            installPhase = ''
              mkdir -p $out/share/java
              cp towerdefense.jar $out/share/java/
              
              # Create launcher script
              cat > $out/bin/towerdefense <<EOF
              #!/bin/sh
              ${jdk}/bin/java -jar $out/share/java/towerdefense.jar "\$@"
              EOF
              
              chmod +x $out/bin/towerdefense
            '';
          };
        };
        
        apps = {
          default = self.apps.${system}.towerdefense;
          
          towerdefense = {
            type = "app";
            program = "${self.packages.${system}.towerdefense}/bin/towerdefense";
          };
        };
        
        devShells.default = pkgs.mkShell {
          buildInputs = [ jdk ];
          shellHook = ''
            echo "Tower Defense development environment"
            echo "Run 'javac TowerDefenseMain.java && jar cfe towerdefense.jar TowerDefenseMain *.class' to build"
            echo "Run 'java -jar towerdefense.jar' to execute"
          '';
        };
      }
    );
}
