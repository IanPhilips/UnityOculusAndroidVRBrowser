# Oculus XR SDK Package

The purpose of this package is to provide Oculus XR Support. This package provides the necessary sdk libraries for users to build Applications that work with the Oculus runtime.
Packages are separated into individual platform support packages. These packages contain the same licenses, but sdk libraries for only the platform are provided.

## Package structure

```
<root>
	com.unity.oculus (Contains all files that are the same across Oculus packages)
		README
		Licenses
		CHANGELOG
		Documentation

	com.unity.oculus.standalone (Contains Standalone specific Oculus files)
		plugins
		package.json

	com.unity.oculus.android (Contains Android specific Oculus files)
		plugins
		package.json
```
