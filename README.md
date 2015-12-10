# Titanium PrintManager
> Titanium Module for accessing the Android [PrintManager](http://developer.android.com/reference/android/print/PrintManager.html)

## Quick Start

### Installation [![gitTio](http://gitt.io/badge.png)](http://gitt.io/component/de.manumaticx.printmanager)
Download the latest distribution ZIP-file and consult the [Titanium Documentation](http://docs.appcelerator.com/titanium/latest/#!/guide/Using_a_Module) on how install it, or simply use the [gitTio CLI](http://gitt.io/cli):

`$ gittio install de.manumaticx.printmanager`

### Usage
```js
var PrintManager = require('de.manumaticx.printmanager');

PrintManager.print({
  // assuming file exists:
  // Alloy:   /app/assets/test.pdf
  // Classic: /Resources/assets/test.pdf
  url: '/test.pdf'
});
```

For now, there is only this on `print` method

## License

[The MIT License (MIT)](LICENSE)