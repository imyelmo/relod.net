/*******************************************************************************
 *    <relod.net: GPLv3 beta software implementing RELOAD - draft-ietf-p2psip-base-26 >
 *    Copyright (C) <2013>  <Marcos Lopez-Samaniego, Isaias Martinez-Yelmo, Roberto Gonzalez-Sanchez> Contact: isaias.martinezy@uah.es
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software Foundation,
 *    Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *******************************************************************************/
   package reload.Common;

   import reload.Common.Exception.ReloadException;
   import reload.Common.Exception.WrongTypeReloadException;
		
   public enum ErrorCode {
   
      invalid("Invalid", (short)0),
      unused("Unused", (short)1),
      error_forbidden("Error Forbidden", (short)2),
      error_not_found("Error Not Found", (short)3),
      error_request_timeout("Error Request Timeout", (short)4),
      error_generation_counter_too_low("Error Generation Counter Too Low", (short)5),
      error_incompatible_with_overlay("Error Incompatible With Overlay", (short)6),
      error_unsupported_forwarding_option("Error Unsupported Forwarding Option", (short)7),
      error_data_too_large("Error Data Too Large", (short)8),
      error_data_too_old("Error Data Too Old", (short)9),
      error_ttl_exceeded("Error TTL Exceeded", (short)10),
      error_message_too_large("Error Message Too Large", (short)11),
      error_unknown_kind("Error Unknown Kind", (short)12),
      error_unknown_extension("Error Unknown Extension", (short)13),
      error_response_too_large("Error Response Too Large", (short)14),
      error_config_too_old("Error Configuration Too Old", (short)15),
      error_config_too_new("Error Configuration Too New", (short)16),
      error_in_progress("Error In Progress", (short)17),
      error_exp_a("Error Exp A", (short)18),
      error_exp_b("Error Exp B", (short)19),
      error_invalid_message("Error Invalid Message", (short)20),
      reserved("Reserved", (short)0x8000);
   
      
      private final String name;
      private final short value;
      
      private ErrorCode(String name, short value) {
      
         this.name = name;
         this.value = value;
      }  
      
      public static ErrorCode valueOf(int value) throws ReloadException{
      
         if(value < 0 || value > 0xffff)
            throw new WrongTypeReloadException("Input value must be a 16-bit positive short number.");
      
         if(value >= 0x8000 && value <= 0xfffe)
            return reserved;
      
         ErrorCode code;
         
         switch (value) {
            case 0:
               code = invalid;
               break;
            case 1:
               code = unused;
               break;
            case 2:
               code = error_forbidden;
               break;
            case 3:
               code = error_not_found;
               break;
            case 4:
               code = error_request_timeout;
               break;
            case 5:
               code = error_generation_counter_too_low;
               break;
            case 6:
               code = error_incompatible_with_overlay;
               break;
            case 7:
               code = error_unsupported_forwarding_option;
               break;
            case 8:
               code = error_data_too_large;
               break;
            case 9:
               code = error_data_too_old;
               break;
            case 10:
               code = error_ttl_exceeded;
               break;
            case 11:
               code = error_message_too_large;
               break;
            case 12:
               code = error_unknown_kind;
               break;
            case 13:
               code = error_unknown_extension;
               break;
            case 14:
               code = error_response_too_large;
               break;
            case 15:
               code = error_config_too_old;
               break;
            case 16:
               code = error_config_too_new;
               break;
            case 17:
               code = error_in_progress;
               break;
            case 18:
               code = error_exp_a;
               break;
            case 19:
               code = error_exp_b;
               break;
            case 20:
               code = error_invalid_message;
               break;     	
            default:
               code = unused;
         }
         
         return code;
      }
      
      public short getValue(){
      
         return value;
      
      }
   	
   }