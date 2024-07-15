PrimeFaces.hideOverlaysOnViewportChange = false;

// MonkeyPatch:
if (PrimeFaces.widget.OverlayPanel) {
    PrimeFaces.widget.OverlayPanel.prototype.handleViewportChange = function() {
        if (PrimeFaces.env.mobile || PrimeFaces.hideOverlaysOnViewportChange === false) {
            this.align(this.target);
        } else {
            this.hide();
        }
    }
}
