self.addEventListener('message', function(e) {
var Module = {
    xml: e.data.xml,
    schema: e.data.schema
  };

postMessage(Module.return);

});