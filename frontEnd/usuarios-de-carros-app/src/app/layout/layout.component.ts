import { Component, AfterViewInit } from '@angular/core';
import jQuery from 'jquery';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})

export class LayoutComponent implements AfterViewInit {

  ngAfterViewInit(){
    (function($) {
        "use strict";
    
        // Add active state to sidbar nav links
        if (typeof window !== 'undefined') {
          var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
              $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
                const anchorElement = this as HTMLAnchorElement;
                  if (anchorElement.href === path) {
                      $(this).addClass("active");
                  }
              });
         
        // Toggle the side navigation
        $("#sidebarToggle").on("click", function(e) {
            e.preventDefault();
            $("body").toggleClass("sb-sidenav-toggled");
        });
      }
    })(jQuery);
  }

}
