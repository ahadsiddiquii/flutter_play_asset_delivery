gen:
	fvm flutter packages pub run build_runner build --delete-conflicting-outputs
	
bundle:
	fvm flutter build appbundle

repair:
	fvm flutter pub cache repair

clean:
	fvm flutter clean

get:
	fvm flutter pub get

pu:
	fvm flutter pub upgrade

doctor:
	fvm flutter doctor -v

sim:
	open -a Simulator

apk_debug:
	fvm flutter build apk --debug
	

